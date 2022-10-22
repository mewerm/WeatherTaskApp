package com.maximmesh.weathertaskapp.utils

import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(string: String?) {
    Toast.makeText(context, string, Toast.LENGTH_LONG).apply {
        setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
        show()
    }
}