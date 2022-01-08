package com.android.example.messapp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MealsListAdapter(val activity: Activity?) : RecyclerView.Adapter<MealsListAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cardView: CardView = view.findViewById(R.id.cardView)
        val mealsTxt: TextView = view.findViewById(R.id.meals)
        val descriptionMeal: TextView = view.findViewById(R.id.description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mealsTxt.text = activity!!.resources.getStringArray(R.array.meals)[position]
//        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
//        val breakfast = sharedPref.getString("breakfast","none")
//        holder.descriptionMeal.text = breakfast
    }

    override fun getItemCount(): Int {
        return 3
    }
    fun deleteItem(position: Int){

    }

    fun contextGet(): Context?{
        return activity
    }
}