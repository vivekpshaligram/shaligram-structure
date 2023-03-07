package com.codestracture.ui.login


sealed interface LoginViewModelEvent {
    data class LoginSuccess(val data: Any): LoginViewModelEvent
    data class LoginError(val message: String) : LoginViewModelEvent
}
