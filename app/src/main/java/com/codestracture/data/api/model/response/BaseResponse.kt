package com.codestracture.data.api.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse<T>(
    @Json(name = "Success") val success: Boolean = false,
    @Json(name = "Message") val message: String? = null,
    @Json(name = "Data") val data: T? = null
)