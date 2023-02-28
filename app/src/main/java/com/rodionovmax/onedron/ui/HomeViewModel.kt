package com.rodionovmax.onedron.ui

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.rodionovmax.onedron.BaseApp
import com.rodionovmax.onedron.model.ResultDto
import com.rodionovmax.onedron.other.DataUiState
import com.rodionovmax.onedron.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val STATE_CATEGORIES = "categories"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repo,
    private val savedStateHandle: SavedStateHandle
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

    init {
        savedStateHandle.get<Array<out String>>(STATE_CATEGORIES)?.let { categories ->
            cacheCategories(categories)
        }
    }

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

    fun cacheCategories(list: Array<out String>) {
        _listOfObjectives.value = list
        savedStateHandle[STATE_CATEGORIES] = list
    }

    private fun emitDataUiState(
        isLoading: Boolean = false,
        results: ResultDto? = null,
        error: String? = null
    ) {
        val dataState = DataUiState(isLoading, results, error)
        _dataUiState.value = dataState
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return HomeViewModel(
                    (application as BaseApp).repo,
                    savedStateHandle
                ) as T
            }
        }
    }

}