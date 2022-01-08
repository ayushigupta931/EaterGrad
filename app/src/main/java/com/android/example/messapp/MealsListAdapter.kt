package com.android.example.messapp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MealsListAdapter(val activity: Activity?, val meals: MutableList<List<String>>) :
    RecyclerView.Adapter<MealsListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealsTxt: TextView = view.findViewById(R.id.meals)
        val descriptionMeal: TextView = view.findViewById(R.id.description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mealsTxt.text = activity!!.resources.getStringArray(R.array.meals)[position]
        if (meals.size > 0)
            holder.descriptionMeal.text = meals[position].toString()
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    fun deleteItem(position: Int) {

    }

    fun contextGet(): Context? {
        return activity
    }
}