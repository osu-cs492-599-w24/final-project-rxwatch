package com.example.cs492_finalproject_rxwatch.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cs492_finalproject_rxwatch.R

class RxSearchFragment : Fragment(R.layout.rx_search_fragment) {
    private lateinit var button: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById(R.id.btn_navigate)

        button.setOnClickListener {
            val directions = RxSearchFragmentDirections.navigateToDrugReport()
            findNavController().navigate(directions)
        }

        //TODO: Implement Search View Screen
    }
}