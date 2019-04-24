package com.unwrappedapps.android.spreadsheet.ui.sheet

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.content.DialogInterface
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.unwrappedapps.android.spreadsheet.R


class JumpToCellFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Use the Builder class for convenient dialog construction
        //val builder = AlertDialog.Builder(activity)

        val dialogContext = context

        if (dialogContext == null) {
            return Dialog(dialogContext)
        }

        val builder = AlertDialog.Builder(dialogContext)

        val inflater = activity?.layoutInflater

        val view = inflater?.inflate(R.layout.jump_dialog, null)

        val editText = view?.findViewById(R.id.cell) as EditText

        val viewModel = activity?.run {
            ViewModelProviders.of(this).get(SheetViewModel::class.java)
        }

        builder.setView(view)
        builder.setMessage(R.string.jump_message)

        var cell : String

        builder
            .setPositiveButton(R.string.jump_go, DialogInterface.OnClickListener { dialog, id ->

                cell = editText.text.toString()

                val regex: Regex = Regex("^[A-Za-z]*[0-9]*")

                if (cell.length > 0 && cell.matches(regex)) { // somewhat matches
                    viewModel?.setTheJumpCell(cell)
                }
            })

            .setNegativeButton(R.string.jump_cancel, DialogInterface.OnClickListener { dialog, id ->
                // User cancelled the dialog
                //Log.d("neg is", "n")
            })

        // Create the AlertDialog object and return it
        return builder.create()

    }
}