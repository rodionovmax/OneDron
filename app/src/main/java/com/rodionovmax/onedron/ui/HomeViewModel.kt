package com.rodionovmax.onedron.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodionovmax.onedron.model.ResultDto
import com.rodionovmax.onedron.other.Converter
import com.rodionovmax.onedron.other.DataUiState
import com.rodionovmax.onedron.repo.Repo
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _dataUiState: MutableStateFlow<DataUiState> = MutableStateFlow(
        DataUiState(
            false,
            null,
            null
        )
    )

    val dataUiState: StateFlow<DataUiState> = _dataUiState.asStateFlow()

    private val _listOfObjectives: MutableStateFlow<Array<out String>> = MutableStateFlow(arrayOf())
    val listOfObjectives: StateFlow<Array<out String>> = _listOfObjectives.asStateFlow()

    fun postRequest(website: String, category: String) {
        viewModelScope.launch {
            runCatching {
                emitDataUiState(isLoading = true)
                repo.fetchData(website, category)
            }.onSuccess { results ->
                if (results == null) {
                    emitDataUiState(error = "No results returned")
                } else {
                    emitDataUiState(results = results)
                }
            }.onFailure {
                emitDataUiState(error = it.message.toString())
            }
        }
    }

    fun cacheObjectives(list: Array<out String>) {
        _listOfObjectives.value = list
    }

    private fun emitDataUiState(
        isLoading: Boolean = false,
        results: ResultDto? = null,
        error: String? = null
    ) {
        val dataState = DataUiState(isLoading, results, error)
        _dataUiState.value = dataState
    }

}