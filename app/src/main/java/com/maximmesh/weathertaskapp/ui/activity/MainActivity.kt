package com.maximmesh.weathertaskapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maximmesh.weathertaskapp.R
import com.maximmesh.weathertaskapp.ui.fragments.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
    }
}