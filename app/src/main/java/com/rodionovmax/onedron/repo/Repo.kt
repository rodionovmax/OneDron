package com.rodionovmax.onedron.repo

import com.rodionovmax.onedron.model.ResultDto

interface Repo {

    suspend fun fetchData(prompt: String, category: String): ResultDto?
}