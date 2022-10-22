package com.maximmesh.weathertaskapp.ui.fragments.main


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.maximmesh.weathertaskapp.R
import com.maximmesh.weathertaskapp.adapters.pager.PagerAdapter
import com.maximmesh.weathertaskapp.data.WeatherModel
import com.maximmesh.weathertaskapp.databinding.FragmentMainBinding
import com.maximmesh.weathertaskapp.ui.fragments.days.DaysFragment
import com.maximmesh.weathertaskapp.ui.fragments.hours.HoursFragment
import com.maximmesh.weathertaskapp.utils.*
import com.squareup.picasso.Picasso
import org.json.JSONObject

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val fragmentList = listOf(            //это список для ViewPager
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private val model: MainViewModel by activityViewModels()
    private val textList = listOf("Hours", "Days")
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermission: String = Manifest.permission.ACCESS_FINE_LOCATION
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
        checkPermission()
        init()
        upDateCurrentWeather()
        getLocation()
    }

    private fun init() = with(binding) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val adapter = PagerAdapter(activity as FragmentActivity, fragmentList) //адаптер для переключений TabLayout
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = textList[position]
        }.attach()

        binding.inputLayout.setEndIconOnClickListener {
            binding.inputEditText.text.toString().let { it1 -> requestWeatherData(it1) } //поиск по городу
        }
    }

    private fun upDateCurrentWeather() = with(binding) {
        model.liveDataCurrent.observe(viewLifecycleOwner) {
            data.text = it.time
            city.text = it.city
            currentTemp.text = convertDegreeToString(it.currentTemp) //конвертация double в градусы string
            Picasso.get().load("https:" + it.imageUrl).into(picture)
        }
    }

    private fun requestWeatherData(name: String) {
        val url = BASE_URL +
                END_POINT +
                "?key=$API_KEY&q=$name&days=7&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                parseWeatherData(result)
            },
            { error ->
                Log.d("@@@", "Volley error: $error")
            }
        )
        queue.add(stringRequest)
    }

    private fun parseWeatherData(result: String) {
        val mainObject = JSONObject(result)
        val list = parseDays(mainObject)
        parseCurrentData(mainObject, list[0])

    }

    private fun parseCurrentData(mainObject: JSONObject, weatherItem: WeatherModel) {
        val item = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current").getDouble("temp_c"),
            mainObject.getJSONObject("current")
                .getJSONObject("condition").getString("icon"),
            weatherItem.hours
        )
        model.liveDataCurrent.value = item
        binding.progressBar.visibility = View.GONE
    }

    private fun parseDays(mainObject: JSONObject): List<WeatherModel> {
        val list = ArrayList<WeatherModel>()

        val daysArray = mainObject.getJSONObject("forecast")
            .getJSONArray("forecastday")

        val name = mainObject.getJSONObject("location").getString("name")

        for (i in 0 until daysArray.length()) {
            val day = daysArray[i] as JSONObject
            val item = WeatherModel(
                name,
                day.getString("date"),
                day.getJSONObject("day").getDouble("avgtemp_c"),
                day.getJSONObject("day").getJSONObject("condition")
                    .getString("icon"),
                day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        model.liveDataList.value = list
        return list
    }

    private fun getLocation() {
        if (!isLocationEnabled()) {
            toast("Для работы приложения необходимо включить GPS")
            return
        }
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                requestWeatherData("${it.result.latitude},${it.result.longitude}")
            }
    }

    private fun isLocationEnabled(): Boolean { //Проверка включен ли GPS
        val locationCheck = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationCheck.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun permissionListener() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getLocation()
            } else {
                explain()
            }
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.dialog_rationale_title))
            .setMessage(resources.getString(R.string.dialog_rationale_message))
            .setPositiveButton(resources.getString(R.string.dialog_rationale_give_access)) { _, _ ->
                permissionLauncher.launch(locationPermission)
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ ->
                dialog.dismiss()
                binding.progressBar.visibility = View.GONE
                toast("${context?.getString(R.string.toast_when_wont_to_giv_location)}")
            }
            .create()
            .show()
    }

    private fun checkPermission() {
        if (!givePermission(locationPermission)) {
            permissionListener()
            permissionLauncher.launch(locationPermission)
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}