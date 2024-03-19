package com.example.cs492_finalproject_rxwatch.ui

import android.content.ClipData.Item
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.database.SearchedDrug
import com.example.cs492_finalproject_rxwatch.data.database.SearchedDrugViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class RxSearchFragment : Fragment(R.layout.rx_search_fragment) {
    private lateinit var searchButton: Button
    private lateinit var searchBox: EditText
    private lateinit var searchedDrugList: RecyclerView

    private val searchedDrugsViewModel: SearchedDrugViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchedDrugList = view.findViewById(R.id.searched_drug_list)
        searchBox = view.findViewById(R.id.et_search_box)
        searchButton = view.findViewById(R.id.btn_navigate)

        searchButton.setOnClickListener {
            val directions = RxSearchFragmentDirections.navigateToDrugReport()
            val query = searchBox.text.toString().lowercase(Locale.ROOT)
            Log.d("RxSearchFragment", "Query from text entry: $query")

            if (!TextUtils.isEmpty(query)) {
                Log.d("RxSearchFragment", "Search query: $query")
                searchedDrugsViewModel.addSearchedDrug(SearchedDrug(
                    query,
                    System.currentTimeMillis()
                ))
                findNavController().navigate(directions)
            } else {
                Snackbar.make(
                    view,
                    "Please enter or select a drug to search.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        searchedDrugList.layoutManager = LinearLayoutManager(requireContext())
        searchedDrugList.setHasFixedSize(true)

        val adapter = SearchedDrugsAdapter(::onRecentlySearchedDrugClicked)
        searchedDrugList.adapter = adapter

        searchedDrugsViewModel.searchedDrugs.observe(viewLifecycleOwner) { drugs ->
            adapter.updateDrugs(drugs.toMutableList())
            searchedDrugList.scrollToPosition(0)
        }

        val itemTouchCallBack =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val searchedDrug = adapter.getItemAt(viewHolder.absoluteAdapterPosition)

                    searchedDrugsViewModel.deleteDrugByName(searchedDrug.drugName)
                }
            }

        ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(searchedDrugList)
    }

    private fun onRecentlySearchedDrugClicked(drug: SearchedDrug) {
        Log.d("RxSearchFragment", "Clicked on drug: $drug")
    }
}