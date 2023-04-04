package com.example.composeuitesting

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.composeuitesting.compose.ComposeFragment
import com.example.composeuitesting.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = ComposeFragment.newInstance()

        supportFragmentManager.beginTransaction().apply {
            replace(binding.contentFrame.id, fragment)
            commit()
        }
    }
}