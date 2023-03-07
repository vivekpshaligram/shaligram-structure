package com.codestracture.utils

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.StringBuilder

class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {

        val response = call.invoke()
        val message = StringBuilder()

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                    message.append("\n")
                }
            }
            message.append("\nError code ${response.code()}")
            throw ApiException(message.toString())
        }
    }
}