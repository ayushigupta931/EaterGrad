package com.android.example.messapp

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MealsFragment(private val position: Int) : Fragment() {
    private val viewModel by viewModels<FirestoreDataFetch>() {
        FirebaseDataFetchViewModelFactory(
            requireActivity().application
        )
    }
    private val dateViewModel by viewModels<DateViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meals, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.mealsRecyclerView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        dateViewModel.init((activity as AppCompatActivity).applicationContext as Application)
        recyclerView.layoutManager = LinearLayoutManager(context)

        if (position in 0..6) {
            viewModel.getMenu(resources.getStringArray(R.array.days)[position])
        } else {
            viewModel.getMenu(resources.getStringArray(R.array.days)[0])
        }

        val recyclerAdapter: MealsListAdapter = MealsListAdapter(context)
        recyclerView.adapter = recyclerAdapter

        viewModel.menuUiModelLiveData.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it, recyclerView)
            progressBar.visibility = View.GONE

        }
        val itemTouchHelper = ItemTouchHelper(MealsItemTouchHelper(recyclerAdapter) { pos, dir ->
            when(dir){
                ItemTouchHelper.RIGHT -> {
                    if(viewModel.menuUiModelLiveData.value!![pos].coming){
                        val builder = AlertDialog.Builder(recyclerAdapter.context)
                        builder.setTitle("Delete")
                        builder.setMessage("Are you sure you want to delete?")
                        builder.setPositiveButton("Confirm") { _, _ ->
                            viewModel.updateUserPref(pos,false)
                        }
                        builder.setNegativeButton("Cancel") { dialog, _ ->
                            dialog?.dismiss()

                        }
                        val dialog = builder.create()
                        dialog.show()
                    }

                }

                ItemTouchHelper.LEFT->{
                    if(!viewModel.menuUiModelLiveData.value!![pos].coming)
                    viewModel.updateUserPref(pos,true)
                }

            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
        viewModel.position = pos
        return view
    }

    companion object {
        var pos: Int = -1

        @JvmStatic
        fun newInstance(position: Int): MealsFragment {
            pos = position
            return MealsFragment(position)
        }
    }
}