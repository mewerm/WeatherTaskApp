package com.maximmesh.weathertaskapp.ui.fragments.days

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.maximmesh.weathertaskapp.adapters.recycler.WeatherAdapter
import com.maximmesh.weathertaskapp.databinding.FragmentDaysBinding
import com.maximmesh.weathertaskapp.ui.fragments.main.MainViewModel


class DaysFragment : Fragment() {

    private var _binding: FragmentDaysBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WeatherAdapter
    private val viewModel: MainViewModel by activityViewModels()

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
        initObserver()
    }

    private fun initRecyclerView() = with(binding) {
        adapter = WeatherAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    private fun initObserver() {
        viewModel.liveDataList.observe(viewLifecycleOwner) {
            adapter.submitList(it.subList(1, it.size))
        }
    }

    companion object {
        fun newInstance() = DaysFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}