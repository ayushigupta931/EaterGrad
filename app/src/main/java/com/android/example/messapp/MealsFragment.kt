package com.android.example.messapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MealsFragment(private val position:Int) : Fragment() {
    private val viewModel by viewModels<FirestoreDataFetch>(){
        FirebaseDataFetchViewModelFactory(
            requireActivity().application
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.app_name), Context.MODE_PRIVATE)
        var data: MenuModel?
        val meals : MutableList<List<String>> = arrayListOf()
        val view = inflater.inflate(R.layout.fragment_meals, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.mealsRecyclerView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(context)

        if(position in 0..6){
            viewModel.getMenu(resources.getStringArray(R.array.days)[position])
//            viewModel.setChoice()
        }else{
            viewModel.getMenu(resources.getStringArray(R.array.days)[0])
        }

        val recyclerAdapter:MealsListAdapter = MealsListAdapter()
        recyclerView.adapter = recyclerAdapter
        viewModel.menuUiModelLiveData.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it, recyclerView)
            progressBar.visibility = View.GONE
            //Menu data coming from backend in the form of 3 lists
//            data = viewModel.menuLiveData.value
//
//            if (data != null) {
//
//                meals.add(data!!.breakfast)
//                meals.add(data!!.lunch)
//                meals.add(data!!.dinner)
//                recyclerAdapter.notifyDataSetChanged()
//            } else
//                progressBar.visibility = View.VISIBLE
        }
        val itemTouchHelper = ItemTouchHelper(MealsItemTouchHelper(recyclerAdapter){pos,dir->
            Toast.makeText(requireContext(),dir.toString(),Toast.LENGTH_SHORT).show()
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
        viewModel.position = pos
        return view
    }

    companion object {
        var pos : Int = -1
        @JvmStatic
        fun newInstance(position: Int): MealsFragment {
            pos = position
            return MealsFragment(position)
        }
    }
}