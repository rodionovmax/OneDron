package com.rodionovmax.onedron.api

import com.rodionovmax.onedron.model.ResultDto
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("default/chatconnector-prompt-endpoint")
    suspend fun postRequest(@Body requestBody: RequestBody): Response<ResultDto>
}

data class FetchDataRequestBody(
    val prompt: String,
    val category: String
)