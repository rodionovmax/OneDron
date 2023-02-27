package com.rodionovmax.onedron.other

import com.rodionovmax.onedron.model.ResultDto

data class DataUiState(
    val isLoading: Boolean,
    val results: ResultDto?,
    val error: String?
)