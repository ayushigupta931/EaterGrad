package com.android.example.messapp

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.util.*

class MealsListAdapter(val context: Context?) :
    RecyclerView.Adapter<MealsListAdapter.ViewHolder>() {

    private var list:List<MenuUiModel> = emptyList()
    private lateinit var recyclerView : RecyclerView

    fun submitList(menuList:List<MenuUiModel>, recyclerView: RecyclerView){
        list = menuList
        this.recyclerView = recyclerView
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView:CardView
        var mealsTxt: TextView
        var descriptionMeal: TextView
        var upArrow: ImageView

        init {
            cardView = view.findViewById(R.id.cardView)
            mealsTxt= view.findViewById(R.id.meals)
            descriptionMeal = view.findViewById(R.id.description)
            upArrow = view.findViewById(R.id.upArrow)

            cardView.setOnClickListener {
                if(cardView.layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT){
                    TransitionManager.beginDelayedTransition(cardView as ViewGroup, AutoTransition())
                    cardView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    descriptionMeal.ellipsize = null
                    descriptionMeal.maxLines = 1000
                    upArrow.visibility = View.VISIBLE
                    TransitionManager.beginDelayedTransition(recyclerView as ViewGroup)
                    notifyDataSetChanged()
                }
            }

            upArrow.setOnClickListener{
                TransitionManager.beginDelayedTransition(cardView as ViewGroup,AutoTransition())
                upArrow.visibility = View.GONE
                cardView.layoutParams.height = 200
                descriptionMeal.ellipsize = TextUtils.TruncateAt.END
                descriptionMeal.maxLines = 1
                TransitionManager.beginDelayedTransition(recyclerView as ViewGroup)
                notifyDataSetChanged()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mealsTxt.text = list[position].title
        holder.descriptionMeal.text = list[position].menu.joinToString(" + ")
        if(list[position].coming){
           holder.cardView.background.setTint(Color.GREEN)
        }else{
            holder.cardView.background.setTint(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}