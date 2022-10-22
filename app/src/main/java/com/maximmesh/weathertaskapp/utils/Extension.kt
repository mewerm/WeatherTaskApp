package com.maximmesh.weathertaskapp.utils

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.givePermission(parameter: String): Boolean { //делаю проверку пермишена и с сравнимаваю int GRANTED == 0 и parameter
    return ContextCompat.checkSelfPermission(
        activity as AppCompatActivity,
        parameter
    ) == PackageManager.PERMISSION_GRANTED

}