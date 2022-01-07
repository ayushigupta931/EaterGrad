package com.android.example.messapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.example.messapp.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tabLayout = binding.tabLayout
        val viewPager = binding.daysViewPager
        val list = resources.getStringArray(R.array.days)
        val adapter = DaysViewPagerAdapter(
            supportFragmentManager,
            lifecycle
        )
        viewPager.adapter = adapter

        val tabLayoutMediator = TabLayoutMediator(
            tabLayout, viewPager, true, true
        ) { tab, position ->
            tab.text = list[position]
        }
        tabLayoutMediator.attach()

    }
}