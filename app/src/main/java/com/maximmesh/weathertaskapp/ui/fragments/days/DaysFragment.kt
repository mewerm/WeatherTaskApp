package com.maximmesh.weathertaskapp.ui.fragments.days

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.maximmesh.weathertaskapp.adapters.recycler.WeatherAdapter
import com.maximmesh.weathertaskapp.data.WeatherModel
import com.maximmesh.weathertaskapp.databinding.FragmentDaysBinding


class DaysFragment : Fragment() {

    private var _binding: FragmentDaysBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() = with(binding){      //Временная проверка отображения
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        recyclerView.adapter = adapter
        val list = listOf(
            WeatherModel(
                "","2222222222222",
                "25ºC","",
                ""),
            WeatherModel(
                "","13:00",
                "25ºC","",
                ""),
            WeatherModel(
                "","14:00",
                "35ºC","",
                "") ,
            WeatherModel(
                "","14:00",
                "35ºC","",
                "") ,
            WeatherModel(
                "","14:00",
                "35ºC","",
                "") ,
            WeatherModel(
                "","14:00",
                "35ºC","",
                "") ,
            WeatherModel(
                "","14:00",
                "35ºC","",
                "") ,
            WeatherModel(
                "","14:00",
                "35ºC","",
                "")
        )
        adapter.submitList(list)
    }

    companion object{
        fun newInstance() = DaysFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}