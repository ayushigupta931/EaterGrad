package com.android.example.messapp.meal

import android.content.Context
import android.graphics.Paint
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
import androidx.recyclerview.widget.RecyclerView
import com.android.example.messapp.R
import com.android.example.messapp.models.MenuUiModel

class MealsListAdapter(val context: Context?) :
    RecyclerView.Adapter<MealsListAdapter.ViewHolder>() {

    private var list: List<MenuUiModel> = emptyList()
    private lateinit var recyclerView: RecyclerView

    fun submitList(menuList: List<MenuUiModel>, recyclerView: RecyclerView) {
        list = menuList
        this.recyclerView = recyclerView
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: CardView
        var mealsTxt: TextView
        var descriptionMeal: TextView

        var mealImage: ImageView

        init {
            cardView = view.findViewById(R.id.cardView)
            mealsTxt = view.findViewById(R.id.meals)
            descriptionMeal = view.findViewById(R.id.description)
            mealImage = view.findViewById(R.id.mealImage)

            cardView.setOnClickListener {
                if (descriptionMeal.maxLines == 3) {
                    TransitionManager.beginDelayedTransition(
                        cardView as ViewGroup,
                        AutoTransition()
                    )
                    descriptionMeal.ellipsize = null
                    descriptionMeal.maxLines = 10000

                    TransitionManager.beginDelayedTransition(recyclerView as ViewGroup)
                    notifyDataSetChanged()
                } else {
                    TransitionManager.beginDelayedTransition(
                        cardView as ViewGroup,
                        AutoTransition()
                    )
                    descriptionMeal.ellipsize = TextUtils.TruncateAt.END
                    descriptionMeal.maxLines = 3
                    TransitionManager.beginDelayedTransition(recyclerView as ViewGroup)
                    notifyDataSetChanged()
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mealsTxt.text = list[position].title
        holder.descriptionMeal.text = list[position].menu.joinToString("\n")
        if (!(list[position].coming))
            holder.mealsTxt.paintFlags =
                holder.mealsTxt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else
            holder.mealsTxt.paintFlags = 0

        when (position) {
            0 -> {
                holder.mealImage.setImageDrawable(context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.food_4
                    )
                })
                context?.let { ContextCompat.getColor(it, R.color.bCard) }
                    ?.let { holder.cardView.background.setTint(it) }
            }
            1 -> {
                holder.mealImage.setImageDrawable(context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.food_3
                    )
                })
                context?.let { ContextCompat.getColor(it, R.color.lCard) }
                    ?.let { holder.cardView.background.setTint(it) }
            }
            2 -> {
                holder.mealImage.setImageDrawable(context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.food_1
                    )
                })
                context?.let { ContextCompat.getColor(it, R.color.dCard) }
                    ?.let { holder.cardView.background.setTint(it) }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}