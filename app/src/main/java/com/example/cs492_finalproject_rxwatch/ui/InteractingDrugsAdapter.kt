package com.example.cs492_finalproject_rxwatch.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.DrugInteractionsDisplay

class InteractingDrugsAdapter(
    private val onDrugInfoItemClick: (DrugInteractionsDisplay) -> Unit
)
    : RecyclerView.Adapter<InteractingDrugsAdapter.DrugInteractionsViewHolder>() {
    private var interactingDrugsList = listOf<DrugInteractionsDisplay>()

    fun updateDrugInteractionsList(newInteractingDrugsList: List<DrugInteractionsDisplay>?) {
        notifyItemRangeRemoved(0, interactingDrugsList.size)
        interactingDrugsList = newInteractingDrugsList ?: listOf()
        Log.d("InteractingDrugsAdapter", "Updated Drug Interactions List: $interactingDrugsList")
        notifyItemRangeInserted(0, interactingDrugsList.size)
    }

    override fun getItemCount() = interactingDrugsList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DrugInteractionsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.interacting_drug_list_item, parent, false)
        return DrugInteractionsViewHolder(itemView, onDrugInfoItemClick)
    }

    override fun onBindViewHolder(
        holder: DrugInteractionsViewHolder,
        position: Int
    ) {
        holder.bind(interactingDrugsList[position])
    }

    class DrugInteractionsViewHolder(
        itemView: View,
        onClick: (DrugInteractionsDisplay) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.tv_name)
        private var currentDrugInfo: DrugInteractionsDisplay? = null

        init {
            itemView.setOnClickListener {
                currentDrugInfo?.let(onClick)
            }
        }

        fun bind(drugDisplay: DrugInteractionsDisplay) {
            currentDrugInfo = drugDisplay

            val drugName = drugDisplay.genericName
            nameTV.text = drugName
        }
    }
}