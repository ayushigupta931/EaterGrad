package com.android.example.messapp.meal

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.messapp.FirebaseDataFetchViewModelFactory
import com.android.example.messapp.FirestoreDataFetch
import com.android.example.messapp.R
import com.android.example.messapp.data.DateViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MealsFragment(private val position: Int) : Fragment() {
    private val viewModel by viewModels<FirestoreDataFetch> {
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
            it?:return@observe
            recyclerAdapter.submitList(it, recyclerView)
            progressBar.visibility = View.GONE

        }
        val format: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY

        val days = arrayOfNulls<String>(7)
        for (i in 0..6) {
            days[i] = format.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        val date = days[position]

        val c: Calendar = Calendar.getInstance()
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        if (date != null) {
            c[Calendar.DAY_OF_MONTH] = date.subSequence(0, 2).toString().toInt()
            c[Calendar.MONTH] = date.subSequence(3, 5).toString().toInt() - 1
            c[Calendar.YEAR] = date.subSequence(6, 10).toString().toInt()
        }
        val difference = (Calendar.getInstance().timeInMillis - c.timeInMillis) / 1000
        var mealTime = -1

        val itemTouchHelper = ItemTouchHelper(MealsItemTouchHelper(recyclerAdapter) { pos, dir ->
            mealTime = when (pos) {
                0 -> {
                    21600
                }
                1 -> {
                    43200
                }
                2 -> {
                    61200
                }
                else -> 0
            }
            when (dir) {
                ItemTouchHelper.RIGHT -> {
                    if (difference > mealTime) {
                        Snackbar.make(
                            requireView(),
                            "You cannot update this meal now",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        if (viewModel.menuUiModelLiveData.value!![pos].coming) {
                            viewModel.updateUserPref(pos, false)
                        }
                    }
                }
                ItemTouchHelper.LEFT -> {
                    if (difference > mealTime) {
                        Snackbar.make(
                            requireView(),
                            "You cannot update this meal now",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        if (!viewModel.menuUiModelLiveData.value!![pos].coming)
                            viewModel.updateUserPref(pos, true)
                    }

                }

            }
            return@MealsItemTouchHelper difference <= mealTime

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