package com.app.cense.ui.hint

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class HintDialogFragment(val text: String) : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Hint")
                .setMessage(text)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}