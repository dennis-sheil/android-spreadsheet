package com.unwrappedapps.android.spreadsheet.ui.sheet

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.unwrappedapps.android.spreadsheet.PullState
import com.unwrappedapps.android.spreadsheet.spreadsheet.Spreadsheet
import java.io.File
import java.io.IOException

class SheetViewModel : ViewModel() {

    val spreadsheet = MutableLiveData<Spreadsheet>()
    val uri = MutableLiveData<Uri>()
    val contentResolver = MutableLiveData<ContentResolver>()

    var leftColumn : Int = 1
    var topRow : Int = 1

    val sheetLoad = MutableLiveData<PullState>()
    val sheetLoadState: LiveData<PullState> = Transformations.switchMap(sheetLoad) {
        readTextFromUri(uri.value, contentResolver.value)
    }


    fun processUri(pUri: Uri, pContentResolver: ContentResolver) {
        uri.value = pUri
        contentResolver.value = pContentResolver
        sheetLoad.value = PullState.LOADING
    }

    @Throws(IOException::class)
    fun readTextFromUri(uri: Uri?, contentResolver: ContentResolver?) : LiveData<PullState> {
        val pullState = MutableLiveData<PullState>()
        pullState.value = PullState.LOADING

        val mimeType = getMimeType(contentResolver, uri)


        if (uri != null) {
            contentResolver?.openInputStream(uri)?.use { inputStream ->
                spreadsheet.value = Spreadsheet(inputStream, mimeType)
                pullState.value = PullState.LOADED_ACK
            }
        }

        return pullState
    }


    fun getMimeType(cr: ContentResolver?, uri: Uri?): String? {
        if (uri?.scheme == ContentResolver.SCHEME_CONTENT) {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(cr?.getType(uri))
        } else {
            return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri?.path)).toString())
        }
    }


}
