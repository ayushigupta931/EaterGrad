package com.android.example.messapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class DaysViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment
        when(position){
            1 -> {fragment = MealsFragment.newInstance() }
            else->{
                return MealsFragment.newInstance()
            }

        }
        return fragment
    }
    override fun getItemCount(): Int {
        return 7
    }
}