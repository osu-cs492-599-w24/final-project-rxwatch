package com.example.cs492_finalproject_rxwatch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.Manufacturer

class ManufacturersListFragment : Fragment(R.layout.manufacturers_list_fragment) {
    private val args: ManufacturersListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drugInfo = args.druginfo
        val drugName = drugInfo.genericName
        val manufacturers = drugInfo.manufacturers
        Log.d("ManufacturersListFragment", "Drug Name: $drugName")
        Log.d("ManufacturersListFragment", "Manufacturers: $manufacturers")
    }
}