package com.codestracture.data.dialog

import androidx.annotation.StringRes

interface IDialog {
    fun showToast(message: String)

    fun showDialog(
        message: String,
        cancelable: Boolean? = true,
        @StringRes positiveButtonId: Int? = null,
        positiveClick: (() -> Unit?)? = null,
        @StringRes negativeButtonId: Int? = null,
        negativeClick: (() -> Unit?)? = null
    )
}