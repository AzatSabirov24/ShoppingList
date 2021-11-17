package com.azat_sabirov.shoppinglist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.azat_sabirov.shoppinglist.R
import com.azat_sabirov.shoppinglist.databinding.NewListDialogBinding

object NewListDialog {
    fun showDialog(context: Context, listener: Listener, name: String) {
        var dialog: AlertDialog? = null
        val dialogBuilder = AlertDialog.Builder(context)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
        dialogBuilder.setView(binding.root)
        binding.apply {
            etNewListName.setText(name)
            if (name.isNotEmpty()){
                btnCreateNewList.text = context.getString(R.string.update)
                tvList.text = context.getString(R.string.edit_note_title)
            }
            btnCreateNewList.setOnClickListener {
                val listName = etNewListName.text.toString()
                if (listName.isNotEmpty()){
                    listener.onClick(listName)
                }
                dialog?.dismiss()
            }
        }
        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener{
        fun onClick(name: String)
    }
}