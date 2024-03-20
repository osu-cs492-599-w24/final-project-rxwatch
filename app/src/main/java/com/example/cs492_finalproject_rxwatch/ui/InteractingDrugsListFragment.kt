package com.example.cs492_finalproject_rxwatch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import com.example.cs492_finalproject_rxwatch.R
import com.example.cs492_finalproject_rxwatch.data.DrugInteractionsDisplay
import com.example.cs492_finalproject_rxwatch.data.Manufacturer
import com.example.cs492_finalproject_rxwatch.data.database.SearchedDrugViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator

/**
 * A fragment to display the list of drugs that interact with the searched drug.
 *
 * This fragment will display a list of drugs that interact with the searched drug.
 * The user can click on a drug to see the manufacturers of the drug that interact with the searched drug.
 * The user can also click on the "Adverse Events" button to navigate to the AdverseEventsFragment.
 * The user can also click on the "Share" button to share the list of drugs that interact with the searched drug.
 *
 * @constructor Creates an InteractingDrugsListFragment
 *
 * @property viewModel DrugInformationViewModel
 *    The ViewModel for the DrugInformationFragment
 *
 * @property searchedDrugsViewModel SearchedDrugViewModel
 *   The ViewModel for the SearchedDrugFragment
 *
 * @property adapter InteractingDrugsAdapter
 *   The adapter for the RecyclerView that will display the list of drugs that interact with the searched drug
 *
 * @property searchResultsListRV RecyclerView
 *  The RecyclerView that will display the list of drugs that interact with the searched drug
 *
 * @property adverseButton Button
 *  The button to navigate to the AdverseEventsFragment
 *
 * @property shareButton Button
 * The button to share the list of drugs that interact with the searched drug
 *
 * @property loadingIndicator CircularProgressIndicator
 * The loading indicator to display when the search is loading
 *
 * @property drugsInfoView View
 * The view to display the list of drugs that interact with the searched drug
 *
 * @property errorMessages TextView
 * The text view to display error messages
 *
 * @property searchedDrugName String
 * The name of the drug that was searched
 *
 * @property onViewCreated Function1<View, Unit>
 *     The function to set up the fragment when the view is created
 *
 * @property onDrugInfoItemClick Function1<DrugInteractionsDisplay, Unit>
 *     The function to handle when a drug is clicked
 *
 * @property buildDrugListShareString Function2<String, List<DrugInteractionsDisplay>, String>
 *     The function to build a string from the list of drugs that interact with the searched drug
 *     that can be shared
 */
class InteractingDrugsListFragment : Fragment(R.layout.interacting_drugs_list_fragment) {
    // Create a DrugInformationViewModel
    private val viewModel: DrugInformationViewModel by viewModels()
    private val searchedDrugsViewModel: SearchedDrugViewModel by viewModels()
    private val adapter = InteractingDrugsAdapter(::onDrugInfoItemClick)

    // The RecyclerView that will display the list of drugs that interact with the searched drug
    private lateinit var searchResultsListRV: RecyclerView

    // The title of the drug that was searched
    private lateinit var drugNameTitle: TextView

    // The button to navigate to the AdverseEventsFragment
    private lateinit var adverseButton: Button
    private lateinit var shareButton: Button

    // The loading indicator to display when the search is loading
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var drugsInfoView: View

    // The text view to display error messages
    private lateinit var errorMessages: TextView

    // The name of the drug that was searched
    private lateinit var searchedDrugName: String

    /**
     * Set up the fragment when the view is created
     *
     * This function will set up the fragment when the view is created.
     * It will set up the RecyclerView and buttons.
     * It will also set up observers for the loading status and error status of the API query.
     *
     * @param view View
     * The view that was created
     *
     * @param savedInstanceState Bundle?
     * The saved instance state
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView and buttons
        drugNameTitle = view.findViewById(R.id.drug_name_title)
        adverseButton = view.findViewById(R.id.btn_navigate_to_adverse)
        shareButton = view.findViewById(R.id.btn_share_interactions)
        loadingIndicator = view.findViewById(R.id.loading_indicator)
        drugsInfoView = view.findViewById(R.id.drugs_info)
        errorMessages = view.findViewById(R.id.tv_error_message)

        // Set up the RecyclerView and buttons
        adverseButton.setOnClickListener {
            val directions = InteractingDrugsListFragmentDirections.navigateToAdverseEvents()
            findNavController().navigate(directions)
        }

        /*
        * The API documentation says to uses `+AND+` to search multiple fields
        * However, Retrofit appears to be URL encoding the `+` signs, so it gets double encoded.
        * Replacing the `+` signs with a space ` ` appears to fix the issue.
        * */
        searchedDrugsViewModel.mostRecentSearchedDrug.observe(viewLifecycleOwner) { drug ->
            // Set up the RecyclerView and buttons
            searchedDrugName = drug[0].drugName // Get the name of the drug that was searched
            drugNameTitle.text = searchedDrugName // Set the title of the drug that was searched
            Log.d("InteractingDrugsListFragment", "Searched drug outside observe: $searchedDrugName")

            val query = "drug_interactions:$searchedDrugName AND _exists_:openfda.generic_name" // Create the query to search for drug interactions
            Log.d("InteractingDrugsListFragment", "Query: $query")

            viewModel.loadDrugInteractions(query) // Load the drug interactions for the given search query

            searchResultsListRV = view.findViewById(R.id.rv_search_results)
            searchResultsListRV.layoutManager = LinearLayoutManager(requireContext())
            searchResultsListRV.setHasFixedSize(true)
            searchResultsListRV.adapter = adapter

            /**
             * The function to handle when a drug is clicked
             *
             * This function will handle when a drug is clicked.
             * It will navigate to the ManufacturersListFragment with the drug that was clicked.
             *
             * @param drugInfo DrugInteractionsDisplay
             * The drug that was clicked
             */
            viewModel.searchResults.observe(viewLifecycleOwner) { drugInformationResults ->
                /**
                 * The function to build a string from the list of drugs that interact with the searched drug
                 * that can be shared
                 *
                 * This function will take a list of DrugInfo objects from the API results and build a string from them
                 * that can be incorporated in a ShareSheet.
                 *
                 * @param searchedDrug String
                 * The name of the drug that was searched
                 *
                 * @param drugList List<DrugInteractionsDisplay>
                 * The list of drugs that interact with the searched drug
                 *
                 * @return String
                 * The string from the list of drugs that interact with the searched drug that can be shared
                 */
                if (drugInformationResults != null) {

                    drugsInfoView.visibility = View.VISIBLE

                    val drugInfoList = drugInformationResults.results // Get the list of drugs that interact with the searched drug

                    /*
                    * Build a map of the manufacturer (mfr) drug names to the drug interactions
                    * and nest it within a map of the generic drug names.
                    * This helps condense the search results, since many manufacturers make the same drug.
                    * */
                    val drugInteractionsMap = mutableMapOf<String, MutableMap<String, String>>()

                    // Iterate through the list of drugs that interact with the searched drug
                    drugInfoList.forEach { drugInfo ->
                        drugInfo.openFDA?.genericName?.forEach { genericName ->
                            val mfrToInteractionsMap = drugInteractionsMap.getOrPut(genericName) {
                                mutableMapOf()
                            }
                            drugInfo.openFDA.manufacturerName?.forEach { mfrName ->
                                val interactionsString =
                                    drugInfo.drugInteractionsString?.joinToString(
                                        separator = "; "
                                    )
                                        ?: "No interactions found"
                                mfrToInteractionsMap[mfrName] = interactionsString
                            }
                        }
                    }

                    // ****** Logging logic for debugging and not necessary for app functionality ******
                    var totalMfrCount = 0
                    drugInteractionsMap.forEach { (_, mfrMap) ->
                        totalMfrCount += mfrMap.size
                    }
                    val genericCount = drugInteractionsMap.size
                    val avgMfrsPerGeneric =
                        if (genericCount > 0) totalMfrCount.toDouble() / genericCount else 0.0
                    Log.d("InteractingDrugsListFragment", "Total Generic Names: $genericCount")
                    Log.d("InteractingDrugsListFragment", "Total Mfr Names: $totalMfrCount")
                    Log.d(
                        "InteractingDrugsListFragment",
                        "Avg Mfr name per Generic Name: $avgMfrsPerGeneric"
                    )
                    // *********************************************************************************

                    // Convert the nested map into nested data classes
                    // so the adapter can more easily work with it
                    val displayList = drugInteractionsMap.map { (genericName, mfrMap) ->
                        DrugInteractionsDisplay(
                            genericName = genericName,
                            manufacturers = mfrMap.map { (mfrName, interactions) ->
                                Manufacturer(mfrName, interactions)
                            }
                        )
                    }

                    // Send the new nested data classes to the adapter
                    adapter.updateDrugInteractionsList(displayList)

                    searchResultsListRV.visibility = View.VISIBLE
                    searchResultsListRV.scrollToPosition(0)

                    // Prep and share the list of drugs
                    shareButton.setOnClickListener {
                        val shareText = buildDrugListShareString(searchedDrugName, displayList)
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        startActivity(Intent.createChooser(intent, null))
                    }
                }
            }
        }

        //Set up observer for loading status of API query
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                drugsInfoView.visibility = View.INVISIBLE
                loadingIndicator.visibility = View.VISIBLE
            }
            else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

        //Set up observer for error status of API query
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                drugsInfoView.visibility = View.INVISIBLE
                errorMessages.visibility = View.VISIBLE
                errorMessages.text = getString(R.string.error_message, error)
            }
        }

    }

    /**
     * The function to handle when a drug is clicked
     *
     * This function will handle when a drug is clicked.
     * It will navigate to the ManufacturersListFragment with the drug that was clicked.
     *
     * @param drugInfo DrugInteractionsDisplay
     * The drug that was clicked
     */
    private fun onDrugInfoItemClick(drugInfo: DrugInteractionsDisplay) {
        Log.d("InteractingDrugsListFragment", "Item clicked: $drugInfo")
        Log.d("InteractingDrugsListFragment", "Clicked Drug Generic Name: ${drugInfo.genericName}")
        Log.d("InteractingDrugsListFragment", "Clicked Drug Manufacturer: ${drugInfo.manufacturers}")
        val directions = InteractingDrugsListFragmentDirections.navigateToManufacturersList(drugInfo, searchedDrugName)
        findNavController().navigate(directions)
    }

    /*
    * Takes a list of DrugInfo objects from the API results and builds a string from them
    * that can be incorporated in a ShareSheet.
    * */
    private fun buildDrugListShareString(searchedDrug: String, drugList: List<DrugInteractionsDisplay>) : String {
        var interactionsString: String = ""
        for(drug in drugList){
            val drugName = drug.genericName
            interactionsString += "\n${drugName}; "
        }
        return getString(R.string.share_text, searchedDrug, interactionsString.dropLast(2))
    }
}