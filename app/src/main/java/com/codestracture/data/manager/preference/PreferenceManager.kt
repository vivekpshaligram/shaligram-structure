package com.codestracture.data.manager.preference

interface PreferenceManager {

    fun setLogin(isLogin: Boolean)
    fun getLogin(): Boolean
}