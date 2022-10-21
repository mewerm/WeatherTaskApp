package com.maximmesh.weathertaskapp.ui.fragments.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maximmesh.weathertaskapp.data.WeatherModel

class MainViewModel : ViewModel(){
    //liveData для текущей погоды
    val liveDataCurrent = MutableLiveData<WeatherModel>()

        //для списка
    val liveDataList = MutableLiveData<List<WeatherModel>>()
}