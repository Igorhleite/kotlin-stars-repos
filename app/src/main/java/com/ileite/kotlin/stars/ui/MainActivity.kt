package com.ileite.kotlin.stars.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ileite.kotlin.stars.R
import com.ileite.kotlin.stars.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}