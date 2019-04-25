package com.unwrappedapps.android.spreadsheet.ui.sheet

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.unwrappedapps.android.spreadsheet.R
import com.unwrappedapps.android.spreadsheet.spreadsheet.Spreadsheet
import kotlinx.android.synthetic.main.sheet_fragment.view.*

class SheetFragment : Fragment() {

    lateinit var fragmentRecyclerView : RecyclerView

    companion object {
        fun newInstance() = SheetFragment()
    }

    private var viewModel: SheetViewModel? = null

    fun startSearch() {
        val sheetLayoutManager = fragmentRecyclerView.layoutManager as SheetLayoutManager
        sheetLayoutManager.startSearch()
    }

    override fun onResume() {
        super.onResume()
        val sheetLayoutManager = fragmentRecyclerView.layoutManager as SheetLayoutManager
        sheetLayoutManager.removeAllViews()
        fragmentRecyclerView.adapter?.notifyDataSetChanged()
        sheetLayoutManager.resetLayoutManagerSearch()
    }

    fun processSearchJump() {
        jumpToNewCoordinates().resetLayoutManagerSearch()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.sheet_fragment, container, false)
        val recyclerView = view.recyclerview

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)

        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val density = displayMetrics.density.toInt()

        val sheetAdapter = SheetAdapter(density, view.select)

        sheetAdapter.assignSpreadsheet(Spreadsheet())

        recyclerView.adapter = sheetAdapter

        val sheetLayoutManager = SheetLayoutManager()
        recyclerView.layoutManager = sheetLayoutManager

        fragmentRecyclerView = recyclerView

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SheetViewModel::class.java)
        }

        val topRow = viewModel?.topRow
        val leftColumn = viewModel?.leftColumn

        if (topRow != null) {
            SheetLayoutManager.topRow = topRow
        }

        if (leftColumn != null) {
            SheetLayoutManager.leftColumn = leftColumn
        }

        viewModel?.sheetLoadState?.observe(this , Observer {
            val sheetAdapter = fragmentRecyclerView.adapter as SheetAdapter
            sheetAdapter.assignSpreadsheet(viewModel?.spreadsheet?.value)
            sheetAdapter.notifyDataSetChanged()
        })

        viewModel?.jumpCell?.observe(this, Observer<String> { item ->
            jumpToNewCoordinates()
        })
    }


    fun jumpToNewCoordinates() : SheetLayoutManager {
        val tr = viewModel?.topRow
        val lc = viewModel?.leftColumn

        if (tr != null && lc != null) {
            SheetLayoutManager.topRow = tr
            SheetLayoutManager.leftColumn = lc
        }

        val sheetAdapter = fragmentRecyclerView.adapter as SheetAdapter
        sheetAdapter.notifyDataSetChanged()

        val sheetLayoutManager = fragmentRecyclerView.layoutManager as SheetLayoutManager
        sheetLayoutManager.removeAllViews()
        return sheetLayoutManager

    }

    fun processUri(uri : Uri, contentResolver: ContentResolver) {
        val sheetLayoutManager = fragmentRecyclerView.layoutManager as SheetLayoutManager
        sheetLayoutManager.resetToTopLeft()
        viewModel?.processUri(uri, contentResolver)
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel?.topRow = SheetLayoutManager.topRow
        viewModel?.leftColumn = SheetLayoutManager.leftColumn
    }


}
