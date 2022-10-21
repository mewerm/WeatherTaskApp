package com.maximmesh.weathertaskapp.ui.fragments.main



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.maximmesh.weathertaskapp.adapters.pager.PagerAdapter
import com.maximmesh.weathertaskapp.databinding.FragmentMainBinding
import com.maximmesh.weathertaskapp.ui.fragments.days.DaysFragment
import com.maximmesh.weathertaskapp.ui.fragments.hours.HoursFragment

class MainFragment : Fragment(){


    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val fragmentList = listOf(            //списки для ViewPager
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private val textList = listOf("HOURS", "DAYS")

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
    }

    private fun init() = with(binding){
        val adapter = PagerAdapter(activity as FragmentActivity, fragmentList) //адаптер для переключений TabLayout
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager){
                tab, position -> tab.text  = textList[position]
        }.attach()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}