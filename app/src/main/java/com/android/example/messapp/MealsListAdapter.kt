package com.android.example.messapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MealsListAdapter(val context: Context?) : RecyclerView.Adapter<MealsListAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cardView: CardView
        init {
            cardView=view.findViewById(R.id.cardView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.cardView.
    }

    override fun getItemCount(): Int {
        return 4
    }
    fun deleteItem(position: Int){

    }

    fun getContext1(): Context?{
        return context
    }
}