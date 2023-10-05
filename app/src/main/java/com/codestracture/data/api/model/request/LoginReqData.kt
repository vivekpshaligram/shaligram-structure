package com.codestracture.data.api.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginReqData(
    @Order(value = 1)
    @Json(name = "password") val password: String? = null,
    @Order(value = 2)
    @Json(name = "email_address") val email: String? = null
)