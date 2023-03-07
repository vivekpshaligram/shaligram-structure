package com.codestracture.data.api.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginReqData(
    @Json(name = "email") val email: String? = null,
    @Json(name = "password") val password: String? = null
)