package com.webomax.openai.common

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import java.io.File

abstract class DownloadImage {

    private var msg: String? = ""
    private var lastMsg = ""


    val downloadImageState = MutableLiveData("")


    @SuppressLint("Range")
    fun downloadImageFromURL(url: String, context: Context) {
        /*val directory = File(Environment.DIRECTORY_DOWNLOADS)


 val direct = File(
          Environment.getExternalStorageDirectory()
             .toString() + "/FOLDER_NAME")

        // change up line code path to our desire path
        if (!directory.exists()) {
            directory.mkdirs()
        }*/



        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)
        val title = getRandomString()
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM,"/ImageAI/$title")


        }
        val directory = File(Environment.DIRECTORY_DCIM , "/ImageAI")
        val dir = (directory)
        if (!dir.exists()){
            dir.mkdirs()
        }
        val file = (directory)
        if (file.exists()){
            file.delete()
            file.createNewFile()
        } else{
            file.createNewFile()
        }



        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(file, status)
                if (msg != lastMsg) {
                    downloadImageState.postValue(msg)
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }.start()


//        val direct = File(
//            Environment.getExternalStorageDirectory()
//                .toString() + "/FOLDER_NAME"
//        )
//        if (!direct.exists()) {
//            direct.mkdirs()
//        }
//        val mgr = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        val downloadUri: Uri = Uri.parse(url)
//        val request = DownloadManager.Request(
//            downloadUri
//        )
//        request.setAllowedNetworkTypes(
//            DownloadManager.Request.NETWORK_WIFI
//                    or DownloadManager.Request.NETWORK_MOBILE
//        )
//            .setAllowedOverRoaming(false).setTitle("Demo")
//            .setDescription("Something useful. No, really.")
//            .setDestinationInExternalPublicDir("/FOLDER_NAME", "test.jpg")
//        mgr.enqueue(request)
    }


    private fun getRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun statusMessage(directory: File, status: Int): String {
        val msg: String = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + "dall_e"
            else -> "There's nothing to download"
        }
        return msg
    }


}