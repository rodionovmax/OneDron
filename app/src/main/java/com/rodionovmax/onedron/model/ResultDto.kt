package com.rodionovmax.onedron.model

import com.google.gson.annotations.SerializedName

data class ResultDto(
    @SerializedName("statusCode") var statusCode: Int,
    @SerializedName("headers") var headers: Header,
    @SerializedName("body") var body: List<String>,
)

data class Header(
    @SerializedName("Content-Type") var contentType: String,
)
