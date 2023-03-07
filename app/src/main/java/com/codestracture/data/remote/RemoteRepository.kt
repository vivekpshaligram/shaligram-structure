package com.codestracture.data.remote

import com.codestracture.data.api.model.request.LoginReqData
import com.codestracture.data.api.model.response.BaseResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface RemoteRepository {

    fun login(reqData: LoginReqData): Flow<BaseResponse<Any>>

    fun userData(): Flow<BaseResponse<Any>>
}