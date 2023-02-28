package com.rodionovmax.onedron.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rodionovmax.onedron.R
import com.rodionovmax.onedron.databinding.FragmentHomeBinding
import com.rodionovmax.onedron.other.Converter
import com.rodionovmax.onedron.other.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var url: String? = null
    private var objective: String? = null
    private var objectivesList: Array<out String> = arrayOf()

    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObjectivesAdapter()
        hideKeyboardOnDropdownPressed()
        selectObjective()

        submitRequest()
        collectResults()
    }

    private fun setObjectivesAdapter() {
        objectivesList = resources.getStringArray(R.array.objectives)
        val objectivesAdapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item, objectivesList
        )
        binding.objectiveTextview.setAdapter(objectivesAdapter)
        viewModel.cacheCategories(objectivesList)
    }

    private fun hideKeyboardOnDropdownPressed() {
        binding.objectiveTextview.setOnClickListener {
            // hide keyboard
            this@HomeFragment.hideKeyboard()
        }
    }

    private fun selectObjective() {
        binding.objectiveTextview.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            when (position) {
                0 -> objective = resources.getString(R.string.tagline)
                1 -> objective = resources.getString(R.string.facebook_ad)
                2 -> objective = resources.getString(R.string.elevator_pitch)
                3 -> objective = resources.getString(R.string.executive_summary)
                4 -> objective = resources.getString(R.string.improve_seo)
                5 -> objective = resources.getString(R.string.sample_review)
                6 -> objective = resources.getString(R.string.hashtags)
                7 -> objective = resources.getString(R.string.chatgpt_recommendations)
                else -> throw ArrayIndexOutOfBoundsException("Index is out of bound")
            }
        }
    }

    private fun submitRequest() {
        binding.submitBtn.setOnClickListener {
            url = binding.websiteInput.editText?.text.toString()

            url?.let { url ->
                objective?.let { objective ->
                    // convert dropdown option to request body parameter
                    val category = Converter.objectiveToRequestCategory(requireContext(), objective)

                    if (url == "") {
                        Toast.makeText(
                            requireContext(),
                            "Website field cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (url != "" && category != "") {
                        viewModel.postRequest(url, category)
                    }
                }
            }
        }
    }

    private fun collectResults() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataUiState.collectLatest {
                    val results = it.results?.body ?: listOf()

                    Timber.d("!!! $results")

                    binding.progressBar.visibility = if (it.isLoading) View.VISIBLE else View.GONE

                    if (results.isNotEmpty()) {
                        binding.resultsRecyclerView.adapter = ResultsAdapter(results)
                    }

                    if (it.error.toString().trim() != "null") {
                        Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /*private fun collectListOfObjectives() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listOfObjectives.collectLatest {
                    objectivesList = it
                }
            }
        }
    }*/

    override fun onResume() {
        super.onResume()
        // to keep exposed dropdown menu after screen rotation
        setObjectivesAdapter()
        // restore camera and date after fragment recreated
        objectivesList = viewModel.listOfObjectives.value
    }

    /*override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArray(STRING_ARRAY, objectivesList)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val objectivesList = savedInstanceState?.getStringArray(STRING_ARRAY) as Array<out String>
        Timber.d("!!! $objectivesList")
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val STRING_ARRAY = "stringArray"
    }
}