package com.android.example.messapp

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.android.example.messapp.databinding.FragmentHomeBinding
import com.android.example.messapp.databinding.FragmentLoginBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_expenses -> {
                // navigate to settings screen
                true
            }
            R.id.action_history -> {
                findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
                true
            }
            R.id.action_sign_out -> {
                Firebase.auth.signOut()
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        
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