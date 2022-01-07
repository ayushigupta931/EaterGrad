package com.android.example.messapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MealsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_meals, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.mealsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val recyclerAdapter = MealsListAdapter(activity)
        recyclerView.adapter=recyclerAdapter
        val itemTouchHelper = ItemTouchHelper(MealsItemTouchHelper(recyclerAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return view
    }


    companion object {
        @JvmStatic fun newInstance(): MealsFragment{
            return MealsFragment()
        }
    }
}