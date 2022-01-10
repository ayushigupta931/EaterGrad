package com.android.example.messapp

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.example.messapp.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: FragmentHomeBinding
    private val dateViewModel by viewModels<DateViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dateViewModel.init((activity as AppCompatActivity).applicationContext as Application)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_expenses -> {
                findNavController().navigate(R.id.action_homeFragment_to_expensesFragment) // navigate to settings screen
                true
            }
            R.id.action_history -> {
                findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
                true
            }
            R.id.action_sign_out -> {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Sign Out")
                builder.setMessage("Are you sure you want to Sign out?")
                builder.setPositiveButton("Confirm") { _, _ ->
                    dateViewModel.deleteAll()
                    Firebase.auth.signOut()
                    lifecycleScope.launch {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.web_client_id))
                            .requestEmail()
                            .build()

                        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                    }
                    googleSignInClient.signOut()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
                builder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog?.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "Mess App"
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