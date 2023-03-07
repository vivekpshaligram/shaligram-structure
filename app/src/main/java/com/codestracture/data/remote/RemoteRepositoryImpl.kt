package com.codestracture.data.remote

import com.codestracture.data.api.Api
import com.codestracture.data.api.model.request.LoginReqData
import com.codestracture.data.api.model.response.BaseResponse
import com.codestracture.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val api: Api
) : RemoteRepository {

    override fun login(reqData: LoginReqData): Flow<BaseResponse<Any>> {
        return flow { emit(SafeApiRequest().apiRequest { api.login(reqData) }) }
    }

    override fun userData(): Flow<BaseResponse<Any>> {
        return flow { emit(SafeApiRequest().apiRequest { api.userData()}) }
    }
}