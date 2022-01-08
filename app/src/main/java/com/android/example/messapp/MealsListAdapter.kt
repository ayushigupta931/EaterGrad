package com.android.example.messapp

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.util.*

class MealsListAdapter :
    RecyclerView.Adapter<MealsListAdapter.ViewHolder>() {

    private var list:List<MenuUiModel> = emptyList()

    fun submitList(menuList:List<MenuUiModel>){
        list = menuList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView:CardView = view.findViewById(R.id.cardView)
        val mealsTxt: TextView = view.findViewById(R.id.meals)
        val descriptionMeal: TextView = view.findViewById(R.id.description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mealsTxt.text = list[position].title
        holder.descriptionMeal.text = list[position].menu.joinToString(",")
//        holder.cardView.visibility=(
//            if(list[position].coming)
//                Visible.
//            else
//                Color.RED
//        )
        holder.cardView.isVisible = list[position].coming
        Calendar.getInstance().time
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun deleteItem(position: Int) {

    }

//    fun contextGet(): Context? {
//        return activity
//    }

}