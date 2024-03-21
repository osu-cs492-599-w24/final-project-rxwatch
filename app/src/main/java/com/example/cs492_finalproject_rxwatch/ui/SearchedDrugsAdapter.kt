package com.example.cs492_finalproject_rxwatch.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.database.SearchedDrug
import okhttp3.internal.http.HttpDate.format
import java.util.Calendar
import java.util.Date

class SearchedDrugsAdapter(
    private val onRecentlySearchedDrugClicked: (SearchedDrug) -> Unit
) : RecyclerView.Adapter<SearchedDrugsAdapter.SearchedDrugsViewHolder>() {
    private var searchedDrugs: MutableList<SearchedDrug> = mutableListOf()

    fun getItemAt(position: Int): SearchedDrug {
        return searchedDrugs[position]
    }

    fun updateDrugs(drugs: MutableList<SearchedDrug>) {
        notifyItemRangeRemoved(0, searchedDrugs.size)
        searchedDrugs = drugs
        notifyItemRangeInserted(0, searchedDrugs.size)
    }

    override fun getItemCount(): Int = searchedDrugs.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedDrugsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.searched_drug_list_item, parent, false)

        return SearchedDrugsViewHolder(view, onRecentlySearchedDrugClicked)
    }

    override fun onBindViewHolder(holder: SearchedDrugsViewHolder, position: Int) {
        holder.bind(searchedDrugs[position])
    }

    class SearchedDrugsViewHolder(
        view: View,
        onClick: (SearchedDrug) -> Unit
        ) : RecyclerView.ViewHolder(view) {
        private val drugTV: TextView = view.findViewById(R.id.tv_drug_name)
        private val timestampTV: TextView = view.findViewById(R.id.tv_timestamp)
        private var currentSearchedDrug: SearchedDrug? = null

        //private member variables for calculating the "x time ago" text
        private val oneSec = 1000L
        private val oneMin: Long = 60 * oneSec
        private val oneHour: Long = 60 * oneMin
        private val oneDay: Long = 24 * oneHour
        private val oneMonth: Long = 30 * oneDay
        private val oneYear: Long = 365 * oneDay

        init {
            itemView.setOnClickListener {
                Log.d("SearchedDrugsViewHolder", "IT: $it")
                currentSearchedDrug?.let(onClick)
            }
        }

        fun bind(searchedDrug: SearchedDrug) {
            currentSearchedDrug = searchedDrug
            drugTV.text = searchedDrug.drugName
            timestampTV.text = calculateTime(searchedDrug.timestamp)
        }

        private fun calculateTime(timestamp: Long): String{
            val date = Date(timestamp)
            val diff = Calendar.getInstance().time.time - date.time

            val diffMin: Long = diff / oneMin
            val diffHours: Long = diff / oneHour
            val diffDays: Long = diff / oneDay
            val diffMonths: Long = diff / oneMonth
            val diffYears: Long = diff / oneYear

            when {
                diffYears > 0 -> {
                    return if(diffYears == 1L) "1 year ago" else "$diffYears years ago"
                }
                diffMonths > 0 -> {
                    return if(diffMonths == 1L) "1 month ago" else "$diffMonths months ago"
                }
                diffDays > 0 -> {
                    return if(diffDays == 1L) "yesterday" else "$diffDays days ago"
                }
                diffHours > 0 -> {
                    return if(diffHours == 1L) "1 hour ago" else "$diffHours hours ago"
                }
                diffMin > 0 -> {
                    return "$diffMin min ago"
                }
                else -> {
                    return "just now"
                }
            }
        }

    }
}