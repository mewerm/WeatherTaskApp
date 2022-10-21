package com.maximmesh.weathertaskapp.ui.fragments.hours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.maximmesh.weathertaskapp.adapters.recycler.WeatherAdapter
import com.maximmesh.weathertaskapp.data.WeatherModel
import com.maximmesh.weathertaskapp.databinding.FragmentHoursBinding
import com.maximmesh.weathertaskapp.ui.fragments.main.MainViewModel
import org.json.JSONArray
import org.json.JSONObject

class HoursFragment: Fragment() {

    private var _binding: FragmentHoursBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WeatherAdapter

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserver()
    }

    private fun initRecyclerView() = with(binding){ //Временная проверка отображения
        adapter = WeatherAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    private fun initObserver() {
        viewModel.liveDataCurrent.observe(viewLifecycleOwner) {
            adapter.submitList(getHoursList(it))
        }
    }

    private fun getHoursList(wItem: WeatherModel) : List<WeatherModel>{
        val hoursArray = JSONArray(wItem.hours)
        val list = ArrayList<WeatherModel>()
        for(i in 0 until hoursArray.length()){
            val item = WeatherModel(
                wItem.city,
                (hoursArray[i] as JSONObject).getString("time"),

                (hoursArray[i] as JSONObject).getString("temp_c"),

                (hoursArray[i] as JSONObject).getJSONObject("condition")
                    .getString("icon"),
                "",
            )
            list.add(item)
        }
        return list
    }
    companion object{
        fun newInstance() = HoursFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}