package com.android.example.messapp.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.messapp.databinding.HistoryCardviewBinding
import com.android.example.messapp.models.mDate

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var mDates: List<mDate> = emptyList<mDate>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            HistoryCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(mDates[position]) {
                binding.date.text = this.date
                if (this.breakfast) {
                    binding.breakfast.text = "Done"
                } else {
                    binding.breakfast.text = "Cancelled"
                }

                if (this.lunch) {
                    binding.lunch.text = "Done"
                } else {
                    binding.lunch.text = "Cancelled"
                }

                if (this.dinner) {
                    binding.dinner.text = "Done"
                } else {
                    binding.dinner.text = "Cancelled"
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return mDates.size
    }

    fun setDates(mDates: List<mDate>) {
        this.mDates = mDates
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: HistoryCardviewBinding) :
        RecyclerView.ViewHolder(binding.root)


}