package com.android.example.messapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.example.messapp.databinding.FragmentHomeBinding
import com.android.example.messapp.databinding.FragmentLoginBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val tabLayout = binding.tabLayout
        val viewPager = binding.daysViewPager
        val list = resources.getStringArray(R.array.days)
        val adapter = DaysViewPagerAdapter(
            requireActivity().supportFragmentManager,
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