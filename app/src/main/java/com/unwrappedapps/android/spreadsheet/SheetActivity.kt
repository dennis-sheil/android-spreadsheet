package com.unwrappedapps.android.spreadsheet

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.unwrappedapps.android.spreadsheet.ui.sheet.SheetFragment

class SheetActivity : AppCompatActivity() {

    val MY_REQ_READ_EXTERNAL_STORAGE : Int = 93


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

    fun load() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, MY_REQ_READ_EXTERNAL_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQ_READ_EXTERNAL_STORAGE) {
                data?.data?.also { uri ->
                    val frag = supportFragmentManager.findFragmentById(R.id.container) as SheetFragment
                    val contentResolver = contentResolver
                    frag.processUri(uri, contentResolver)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_spreadsheet, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                val frag = supportFragmentManager.findFragmentById(R.id.container) as SheetFragment
                frag.doSearch()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                return true
            }
        })

        return true
    }

}
