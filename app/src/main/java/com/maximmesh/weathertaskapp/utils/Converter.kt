package com.maximmesh.weathertaskapp.utils

fun convertDegreeToString(degree: Double): String {
    var result = "${degree.toInt()}\u2103"
    if (degree >= 1.0) {
        result = "+$result"
    }
    return result
}