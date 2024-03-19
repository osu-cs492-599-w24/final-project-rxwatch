package com.example.cs492_finalproject_rxwatch.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.Manufacturer

class MfrDrugInteractionsAdapter(
    var context: Context,
    private var manufacturers: MutableList<Manufacturer>
) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return manufacturers.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Manufacturer {
        return manufacturers[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return manufacturers[groupPosition].drugInteractions
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertedView = convertView
        if (convertedView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertedView = inflater.inflate(R.layout.manufacturers_group, null)
        }
        var itemChild = convertedView!!.findViewById<TextView>(R.id.tv_mfr_group)
        itemChild.text = getGroup(groupPosition).manufacturerName

        return convertedView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertedView = convertView
        if (convertedView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertedView = inflater.inflate(R.layout.drug_interactions_list_item, null)
        }
        var itemChild = convertedView!!.findViewById<TextView>(R.id.tv_drug_interactions)
        itemChild.text = getGroup(groupPosition).drugInteractions

        return convertedView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

}