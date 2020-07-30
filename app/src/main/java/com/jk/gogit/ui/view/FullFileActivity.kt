package com.jk.gogit.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.jk.codeview.Codeview
import com.jk.gogit.R
import com.jk.gogit.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_full_file.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream


class FullFileActivity : BaseActivity() {

    private val path by lazy { intent.getStringExtra("path") }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_full_file
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val login = intent.getStringExtra("login")
        val repoName = intent.getStringExtra("repoName")
        val fileSha = intent.getStringExtra("file_sha")

        enableHomeInToolBar(Utils.getAbsolutePath(path), true)
        if (Utils.isImage(path) || !Utils.isNotDownloadableFileFormat(path)) {
            doRequest(login, repoName, fileSha)
            //showNoContentView(false)
        } else {
            //showNoContentView(true)
            onError(null)
            invalidateOptionsMenu()
        }
    }

    /*  private fun showNoContentView(isVisible: Boolean) {
          if (isVisible)
              txt_no_content.visibility = View.VISIBLE
          else
              txt_no_content.visibility = View.GONE
      }*/

    private fun doRequest(login: String, repoName: String, file_sha: String) {

        if (Utils.isImage(path)) {
            subscriptions.add(model.getFullFileAsBlob(login, repoName, file_sha)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val decodedString = Base64.decode(it.content, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        img_fullscreen_content.visibility = View.VISIBLE
                        img_fullscreen_content.setImageBitmap(bitmap)
                        web_fullscreen_content.visibility = View.GONE
                        //      fullscreen_content.loadDataWithBaseURL("", it, "text/html", "UTF-8", "")
                        // print(it)

                    }, {
                        onError(it)
                        it.printStackTrace()
                    }))


        } else {
            var baseUrl = ""
            subscriptions.add(model.getFullFile(login, repoName, path)
                    .flatMap {
                        baseUrl = it.download_url.removeSuffix("/README.MD")
                        model.getFullFileAsHTML(login, repoName, path)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        render(it, baseUrl)

                        //      fullscreen_content.loadDataWithBaseURL("", it, "text/html", "UTF-8", "")
                        // print(it)

                    }, {
                        it.printStackTrace()
                    }))
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun render(decodedStr: String, baseUrl: String) {
        //val path = intent.getStringExtra("path")
        //val isImage = Utils.isImage(path)
        img_fullscreen_content.visibility = View.GONE

        web_fullscreen_content.apply {
            visibility = View.VISIBLE


        }
        Codeview.with(applicationContext)
                .withCode(decodedStr)
                .setAutoWrap(true)
                .into(web_fullscreen_content, baseUrl)
        /*    web_fullscreen_content.settings.apply {
              javaScriptEnabled = true
               layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
               setAppCachePath(cacheDir.path)
               setAppCacheEnabled(true)
               cacheMode = WebSettings.LOAD_NO_CACHE
               loadWithOverviewMode = true
               useWideViewPort = true
               setSupportZoom(true)
               builtInZoomControls = true
               displayZoomControls = false
           }*/
        /* val d = Utils.generateHtmlSourceHtml(decodedStr, "", "")
         web_fullscreen_content.apply {
             visibility = View.VISIBLE
             if (Build.VERSION.SDK_INT > 21)
                 setLayerType(View.LAYER_TYPE_HARDWARE, null)

             scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
             setInitialScale(1)
             loadUrl("about:blank")
             loadDataWithBaseURL(baseUrl, d, "text/html", "UTF-8", "")

         }*/


    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_download)?.isVisible = true
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_download -> {
                checkPermissionAndDownload()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkPermissionAndDownload() {
        if (checkPermission()) {
            startDownload()
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startDownload()
        } else {
            Snackbar.make(txt_no_content, "Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show()
        }

    }

    private fun decodeBase64String(encodedString: String): ByteArray {
        return Base64.decode(encodedString, Base64.DEFAULT)
    }

    private fun startDownload() {
        val gitUrl = intent.getStringExtra("gitUrl")
        val filename = getFilePath()

        showNotification("Download is in Progress", filename)
        subscriptions.add(model.getRawFileContent(gitUrl)
                .map {
                    decodeBase64String(JSONObject(it).getString("content"))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    writeBytesToFile(it)
                }, {
                    showNotification("Error in Download", filename)
                    it.printStackTrace()
                }))

    }

    private fun writeBytesToFile(decodedArray: ByteArray) {
        try {
            val fileOut = getFilePath()
            if (fileOut != null) {
                if (!fileOut.exists())
                    fileOut.createNewFile()
                val fileOutputStream = FileOutputStream(fileOut)
                fileOutputStream.write(decodedArray)
                showNotification("Download Completed, Tap to open", fileOut)
            } else {
                toast("Unable to write file to your SD card..")
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun getFilePath(): File? {
        val downloadFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        return if (downloadFolderPath.exists()) {
            val folderPath = File(downloadFolderPath, "/${resources.getString(R.string.app_name)}")
            if (!folderPath.exists())
                folderPath.mkdirs()
            val absPath = Utils.getAbsolutePath(path)
            File(folderPath.absolutePath, "/$absPath")
        } else null
    }

    private fun showNotification(message: String, path: File?) {
        val openFile = Intent(Intent.ACTION_GET_CONTENT)
        if (path != null) {
            val mime = MimeTypeMap.getSingleton()
            val ext = path.name.substring(path.name.indexOf(".") + 1)
           val type = mime.getMimeTypeFromExtension(ext)
            val dataUri = FileProvider.getUriForFile(this,
                    getString(R.string.file_provider_authority),
                    path)
            //Uri.fromFile(path)
            openFile.setDataAndType(dataUri, type)
            openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
           //openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel("NOTIFICATION_CHANNEL_ID", resources.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)

            // Configure the notification channel.
            //notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            val builder = NotificationCompat.Builder(this@FullFileActivity, notificationChannel.id)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    .setContentTitle(path?.name)
                    .setContentText(message)
            if (path != null)
                builder.setContentIntent(PendingIntent.getActivity(ctx, 1, openFile, PendingIntent.FLAG_UPDATE_CURRENT))
            builder
        } else {
            val builder = NotificationCompat.Builder(this@FullFileActivity, "NOTIFICATION_CHANNEL_ID")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    .setContentTitle(path?.name)
                    .setContentText(message)
            if (path != null)
                builder.setContentIntent(PendingIntent.getActivity(ctx, 1, openFile, PendingIntent.FLAG_UPDATE_CURRENT))
            builder
        }

        if (path == null)
            mBuilder.setAutoCancel(false)
        else
            mBuilder.setAutoCancel(true)

        notificationManager.notify(455, mBuilder.build())


    }
}
