package com.codestracture.ui.login

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.codestracture.R
import com.codestracture.data.api.model.request.LoginReqData
import com.codestracture.data.local.LocalRepository
import com.codestracture.data.remote.RemoteRepository
import com.codestracture.ui.base.BaseViewModel
import com.codestracture.utils.ApiResult
import com.codestracture.utils.SingleLiveEvent
import com.codestracture.utils.ext.isValidEmail
import com.codestracture.utils.ext.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : BaseViewModel() {

    val viewModelEvent: SingleLiveEvent<LoginViewModelEvent> = SingleLiveEvent()

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    val emailAddressErrorMessage = MediatorLiveData<Int>()
        .apply {
            addSource(email) {
                var isValid = it.isNotEmpty()
                val message = if (isValid) {
                    isValid = it.isValidEmail()
                    if (isValid) {
                        null
                    } else {
                        R.string.invalid_email
                    }
                } else {
                    R.string.mandatory_field
                }

                setValue(message)
            }
        }

    val passwordErrorMessage = MediatorLiveData<Int>()
        .apply {
            addSource(password) {
                var isValid = it.isNotEmpty()
                val message = if (isValid) {
                    isValid = it.isValidPassword()
                    if (isValid) {
                        null
                    } else {
                        R.string.invalid_password
                    }
                } else {
                    R.string.mandatory_field
                }

                setValue(message)
            }
        }

    private fun isValidate(): Boolean {
        var isValid = true

        if (email.value?.isNotEmpty() != true) {
            email.postValue("")
            isValid = false
        }

        if (password.value?.isNotEmpty() != true) {
            password.postValue("")
            isValid = false
        }

        return isValid
    }

    fun doLogin() {
        viewModelScope.launch(MAIN) {
            if (isValidate()) {
                val email = email.value ?: ""
                val password = password.value ?: ""

                LoginReqData(email, password).also { reqData ->
                    remoteRepository.login(reqData)
                        // .retry(2) { cause -> cause is NoInternetException }
                        // .retryWhen { cause, attempt ->  && attempt < 2 }
                        .flowOn(IO)
                        .catch {
                            Log.d("MyTag","Login Response Error: ${it.message}")
                            viewModelEvent.postValue(LoginViewModelEvent.LoginError(it.message.toString()))
                        }.collect {
                            Log.d("MyTag","Login Response: ${it.success} : ${it.data}")
                            if (it.success && it.data != null) {
                                viewModelEvent.postValue(LoginViewModelEvent.LoginSuccess(it))
                            } else {
                                viewModelEvent.postValue(LoginViewModelEvent.LoginError(it.message.toString()))
                            }
                        }
                }
            }
        }
    }

    fun preformLoginWithLiveData() = liveData {
        emit(ApiResult.Loading)
        remoteRepository.userData()
            .flowOn(Dispatchers.IO)
            .catch {
                emit(ApiResult.Error(it.message.toString()))
            }.collect {
                emit(ApiResult.Success(it))
            }
    }

    fun preformLogin(reqData: LoginReqData) {
        viewModelScope.launch(Dispatchers.Main) {
            remoteRepository.login(reqData)
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("MyTag", "Catch: ${it.message}")
                }
                .collect {
                    Log.d("MyTag", "Collect Success")
                }
        }
    }

    fun preformLoginWithUserData(reqData: LoginReqData) {
        viewModelScope.launch(Dispatchers.Main) {
            remoteRepository.login(reqData)
                .flatMapConcat {
                    Log.d("MyTag", "flatMapConcat")
                    remoteRepository.userData()
                }
                .flowOn(Dispatchers.IO)
                .catch {
                    Log.d("MyTag", "Catch: ${it.message}")
                }.collect {
                    Log.d("MyTag", "Collect Success")
                }
        }
    }
}