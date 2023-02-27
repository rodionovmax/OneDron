package com.rodionovmax.onedron.repo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rodionovmax.onedron.api.ApiService
import com.rodionovmax.onedron.api.FetchDataRequestBody
import com.rodionovmax.onedron.model.ResultDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val apiService: ApiService
) : Repo {

    override suspend fun fetchData(prompt: String, category: String): ResultDto? {
        // Convert FetchDataRequestBody object to OkHttp Request Body
        val requestBodyJson: String = Gson().toJson(FetchDataRequestBody(prompt, category))
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), requestBodyJson)

        var res: ResultDto? = null
        val response = apiService.postRequest(requestBody)
        val body = response.body()
        body?.let { res = body }
        return res
    }


}