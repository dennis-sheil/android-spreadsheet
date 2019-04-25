package com.unwrappedapps.android.spreadsheet

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.unwrappedapps.android.spreadsheet.ui.sheet.JumpToCellFragment
import com.unwrappedapps.android.spreadsheet.ui.sheet.SheetFragment
import kotlinx.android.synthetic.main.sheet_fragment.*
import android.app.SearchManager
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.unwrappedapps.android.spreadsheet.spreadsheet.Spreadsheet
import com.unwrappedapps.android.spreadsheet.ui.sheet.SheetViewModel
import java.lang.ref.WeakReference


class SheetActivity : AppCompatActivity() {

    val MY_REQ_READ_EXTERNAL_STORAGE : Int = 93 // random magic number

    var lastSearch : LastSearch = resetLastSearch()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sheet_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SheetFragment.newInstance())
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_jump -> {
                jumpDialog()
                return true
            }
            R.id.action_search -> {
                return true
            }
            R.id.action_load -> {
                load()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun jumpDialog() {
        val newFragment = JumpToCellFragment()
        newFragment.show(supportFragmentManager, "jump")
    }


    fun load() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, MY_REQ_READ_EXTERNAL_STORAGE)
    }

    fun clearSelectBox() {
        select.text = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQ_READ_EXTERNAL_STORAGE) {
                clearSelectBox()
                data?.data?.also { uri ->
                    val frag = supportFragmentManager.findFragmentById(R.id.container) as SheetFragment
                    val contentResolver = contentResolver
                    frag.processUri(uri, contentResolver)
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_spreadsheet, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.getActionView() as SearchView
        val menuItem = menu.findItem(R.id.action_search)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setSubmitButtonEnabled(true)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                search(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                return false
            }
        })

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                lastSearch = resetLastSearch()
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                startSearch()
                return true
            }
        })

        return true
    }


    fun search(string: String) {

        val viewModel = ViewModelProviders.of(this).get(SheetViewModel::class.java)
        val sheet = viewModel.spreadsheet.value

        sheet?.let {
            val searchData = SearchData(string, it, lastSearch)
            SearchTask(this).execute(searchData)
        }
    }


    fun startSearch() {
        val frag = supportFragmentManager.findFragmentById(R.id.container) as SheetFragment
        frag.startSearch()
    }

    fun doSearchJump() {
        val viewModel = ViewModelProviders.of(this).get(SheetViewModel::class.java)

        // need to add space for column and row markers
        viewModel.leftColumn = lastSearch.column+1
        viewModel.topRow = lastSearch.row+1

        val fragment = supportFragmentManager.findFragmentById(R.id.container) as SheetFragment
        fragment.processSearchJump()
    }

    fun resetLastSearch() : LastSearch {
        return LastSearch(0,0,"")
    }


    data class SearchData(val string: String, val sheet: Spreadsheet, val lastSearch: LastSearch)
    data class LastSearch(val row: Int, val column: Int, val word: String)

}
