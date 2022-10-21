package com.maximmesh.weathertaskapp.ui.fragments.main


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayoutMediator
import com.maximmesh.weathertaskapp.adapters.pager.PagerAdapter
import com.maximmesh.weathertaskapp.data.WeatherModel
import com.maximmesh.weathertaskapp.databinding.FragmentMainBinding
import com.maximmesh.weathertaskapp.ui.fragments.days.DaysFragment
import com.maximmesh.weathertaskapp.ui.fragments.hours.HoursFragment
import com.maximmesh.weathertaskapp.utils.Constants.API_KEY
import com.maximmesh.weathertaskapp.utils.Constants.BASE_URL
import com.maximmesh.weathertaskapp.utils.Constants.END_POINT
import com.squareup.picasso.Picasso
import org.json.JSONObject


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val fragmentList = listOf(            //списки для ViewPager
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private val textList = listOf("HOURS", "DAYS")

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        upDateCurrentWeather()
        requestWeatherData("Butugychag") //Проверка работы запрос-ответ для указанного города
    }

    private fun init() = with(binding) {
        val adapter = PagerAdapter(activity as FragmentActivity, fragmentList) //адаптер для переключений TabLayout
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = textList[position]
        }.attach()
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

    private fun upDateCurrentWeather() = with(binding) {
        viewModel.liveDataCurrent.observe(viewLifecycleOwner) {
            data.text = it.time
            city.text = it.city
            currentTemp.text = it.currentTemp
            Picasso.get().load("https:" + it.imageUrl).into(picture)
        }
    }

    private fun parseCurrentData(mainObject: JSONObject, weatherItem: WeatherModel) {
        val item = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current").getString("temp_c"),
            mainObject.getJSONObject("current")
                .getJSONObject("condition").getString("icon"),
            weatherItem.hours
        )
        viewModel.liveDataCurrent.value = item
    }

    fun parseDays(mainObject: JSONObject): List<WeatherModel> {
        val list = ArrayList<WeatherModel>()

        val daysArray = mainObject.getJSONObject("forecast")
            .getJSONArray("forecastday")

        val name = mainObject.getJSONObject("location").getString("name")

        for (i in 0 until daysArray.length()) {
            val day = daysArray[i] as JSONObject
            val item = WeatherModel(
                name,
                day.getString("date"),
                day.getJSONObject("day").getString("avgtemp_c"),
                day.getJSONObject("day").getJSONObject("condition")
                    .getString("icon"),
                day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        viewModel.liveDataList.value = list
        return list
    }
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}