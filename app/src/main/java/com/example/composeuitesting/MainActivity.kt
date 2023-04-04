package com.example.composeuitesting

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.composeuitesting.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}