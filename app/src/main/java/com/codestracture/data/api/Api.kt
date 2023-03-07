package com.codestracture.data.api

import com.codestracture.data.api.model.request.LoginReqData
import com.codestracture.data.api.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("AuthenticateMobileUser")
    suspend fun login(@Body reqData: LoginReqData): Response<BaseResponse<Any>>

    @POST("AuthenticateMobileUser")
    suspend fun userData(): Response<BaseResponse<Any>>

}