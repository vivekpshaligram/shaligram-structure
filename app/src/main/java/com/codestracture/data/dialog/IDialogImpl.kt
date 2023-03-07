package com.codestracture.data.dialog


import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

class IDialogImpl(
    private val context: Context
) : IDialog {

    private var dialog: AlertDialog.Builder? = null

    override fun showToast(message: String) {
        Log.d("MyTag", "showToast: ${context.javaClass}")
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showDialog(
        message: String,
        cancelable: Boolean?,
        positiveButtonId: Int?,
        positiveClick: (() -> Unit?)?,
        negativeButtonId: Int?,
        negativeClick: (() -> Unit?)?
    ) {
        if (dialog == null) {
            dialog = AlertDialog.Builder(context)
        }
        dialog?.apply {
            setMessage(message)
            cancelable?.let { setCancelable(it) }
            positiveButtonId?.let {
                setPositiveButton(it) { _: DialogInterface, _: Int ->
                    if (positiveClick != null) {
                        positiveClick()
                    }
                }
            }
            negativeButtonId?.let {
                setNegativeButton(it) { _: DialogInterface, _: Int ->
                    if (negativeClick != null) {
                        negativeClick()
                    }
                }
            }
        }?.create()?.show()
    }
}