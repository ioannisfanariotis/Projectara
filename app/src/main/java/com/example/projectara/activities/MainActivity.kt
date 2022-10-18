package com.example.projectara.activities

import android.os.Bundle
import com.example.projectara.R
import com.example.projectara.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}