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

    var cell : String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Use the Builder class for convenient dialog construction
        //val builder = AlertDialog.Builder(activity)

        val c = context

        if (c == null) {
            return Dialog(c)
        }

        val builder = AlertDialog.Builder(c)

        val inflater = activity?.layoutInflater

        //val v = inflater.inflate(R.layout.jump_dialog, null)

        val v = inflater?.inflate(R.layout.jump_dialog, null)

        val et = v?.findViewById(R.id.cell) as EditText

        val viewModel = activity?.run {
            ViewModelProviders.of(this).get(SheetViewModel::class.java)
        }

        builder.setView(v)
        builder.setMessage("Jump to cell:")

        builder
            .setPositiveButton("Go", DialogInterface.OnClickListener { dialog, id ->
                //String ss = et.getText().toString();

                cell = et.text.toString()

                // Go to cell
                //Log.d("sell is", "c")
                //Log.d("cells is", "" + cell.length + "")
                //Log.d("cell is", cell)

                val rx: Regex = Regex("^[A-Za-z]*[0-9]*")

                if (cell.length > 0 && cell.matches(rx)) { // somewhat matches
                    viewModel?.setTheJumpCell(cell)
                }
            })

            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                // User cancelled the dialog
                //Log.d("neg is", "n")
            })

        // Create the AlertDialog object and return it
        return builder.create()

    }
}