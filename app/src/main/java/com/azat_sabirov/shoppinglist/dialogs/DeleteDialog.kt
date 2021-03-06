package com.azat_sabirov.shoppinglist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.azat_sabirov.shoppinglist.databinding.DeleteItemDialogBinding

object DeleteDialog {
    fun showDialog(context: Context, listener: Listener) {
        var dialog: AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(context)
        val binding = DeleteItemDialogBinding.inflate(LayoutInflater.from(context))
        dialogBuilder.setView(binding.root)
        binding.apply {
            btnDelete.setOnClickListener {
                listener.onClick()
                dialog?.dismiss()
            }
            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}