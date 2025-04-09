package com.codestracture.data.manager.preference

interface PreferenceManager {

    fun setLogin(isLogin: Boolean)
    fun getLogin(): Boolean

    fun getPrimaryColor(): String?
    fun setPrimaryColor(color: String)

    fun getSecondaryColor(): String?
    fun setSecondaryColor(color: String)

    fun getButtonTextColor(): String?
    fun setButtonTextColor(color: String)
}
