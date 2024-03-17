package com.example.cs492_finalproject_rxwatch.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.DrugInfo
import com.example.cs492_finalproject_rxwatch.data.DrugInformation

class DrugInteractionsAdapter(
    private val onDrugInfoItemClick: (DrugInformation) -> Unit // This DrugInformation will most likely have to change
)
    : RecyclerView.Adapter<DrugInteractionsAdapter.DrugInteractionsViewHolder>() {
        private var drugInteractionsList = listOf<DrugInfo>()

    fun updateDrugInteractionsList(newDrugInteractionsList: List<DrugInfo>?) {
        notifyItemRangeRemoved(0, drugInteractionsList.size)
        drugInteractionsList = newDrugInteractionsList ?: listOf()
        Log.d("DrugInteractionsAdapter", "Updated Drug Interactions List: $drugInteractionsList")
        notifyItemRangeInserted(0, drugInteractionsList.size)
    }

    override fun getItemCount() = drugInteractionsList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DrugInteractionsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.drug_interactions_list_item, parent, false)
        return DrugInteractionsViewHolder(itemView, onDrugInfoItemClick)
    }

    override fun onBindViewHolder(
        holder: DrugInteractionsViewHolder,
        position: Int
    ) {
        holder.bind(drugInteractionsList[position])
    }

    class DrugInteractionsViewHolder(
        itemView: View,
        onClick: (DrugInformation) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.tv_name)
//        private val interactionsTV: TextView = itemView.findViewById(R.id.tv_interactions)
        private var currentDrugInfo: DrugInfo? = null

        init {
            itemView.setOnClickListener {
//                currentDrugInfo?.let(onClick)     // TODO
            }
        }

        fun bind(drugInfo: DrugInfo) {
            currentDrugInfo = drugInfo

            val drugName = drugInfo.openFDA?.genericName
            nameTV.text = drugName.toString()

//            val drugInteractions = drugInfo.drugInteractionsString
//            interactionsTV.text = drugInteractions.toString()
        }
    }
}