package com.android.example.messapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MealsFragment(private val position:Int) : Fragment() {
    private val viewModel by viewModels<FirestoreDataFetch>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.app_name), Context.MODE_PRIVATE)
        var data: MenuModel?
        val view = inflater.inflate(R.layout.fragment_meals, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.mealsRecyclerView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(context)

        if(position in 0..6){
            viewModel.getMenu(resources.getStringArray(R.array.days)[position])
        }else{
            viewModel.getMenu(resources.getStringArray(R.array.days)[0])
        }


        viewModel.menuLiveData.observe(viewLifecycleOwner) {
            //Menu data coming from backend in the form of 3 lists
            data = viewModel.menuLiveData.value

            if (data != null) {
                progressBar.visibility = View.GONE
//                val editor = sharedPref.edit()
//                editor.putString("breakfast", data!!.breakfast.toString())
//                editor.apply()
            } else
                progressBar.visibility = View.VISIBLE
        }
        val recyclerAdapter = MealsListAdapter(activity)
        recyclerView.adapter = recyclerAdapter
        val itemTouchHelper = ItemTouchHelper(MealsItemTouchHelper(recyclerAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(position: Int): MealsFragment {
            return MealsFragment(position)
        }
    }
}