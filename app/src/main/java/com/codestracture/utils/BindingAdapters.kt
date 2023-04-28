package com.codestracture.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object BindingAdapters {

    @BindingAdapter("app:errorText")
    @JvmStatic
    fun setErrorMessage(view: TextInputLayout, errorMessage: Int?) {
        view.error = errorMessage?.let { view.context.getString(it) }
    }
}