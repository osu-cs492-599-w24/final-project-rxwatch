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
            var result = ""
            val diff = Calendar.getInstance().time.time - date.time

            val oneSec = 1000L
            val oneMin: Long = 60 * oneSec
            val oneHour: Long = 60 * oneMin
            val oneDay: Long = 24 * oneHour
            val oneMonth: Long = 30 * oneDay
            val oneYear: Long = 365* oneDay

            val diffMin: Long = diff / oneMin
            val diffHours: Long = diff / oneHour
            val diffDays: Long = diff / oneDay
            val diffMonths: Long = diff / oneMonth
            val diffYears: Long = diff / oneYear

            when {
                diffYears > 0 -> {
                    result = "$diffYears years ago"
                }
                diffMonths > 0 -> {
                    result = "$diffMonths months ago"
                }
                diffDays > 0 -> {
                    result = "$diffDays days ago"
                }
                diffHours > 0 -> {
                    result = "$diffHours hours ago"
                }
                diffMin > 0 -> {
                    result = "$diffMin min ago"
                }
                else -> {
                    result = "just now"
                }

            }

            return result

        }

    }
}