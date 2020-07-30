package com.jk.gogit.utils

import android.net.Uri
import android.webkit.MimeTypeMap
import com.jk.gogit.model.File
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern


object Utils {

    private val IMAGE_EXTENSIONS = arrayOf(".png", ".jpg", ".jpeg", ".svg")
    // private val MARKDOWN_EXTENSIONS = arrayOf(".md", ".mkdn", ".mdwn", ".mdown", ".markdown", ".mkd", ".mkdown", ".ron", ".rst", "adoc")
    private val ARCHIVE_EXTENSIONS = arrayOf(".gif", ".mp4", ".mp3", ".zip", ".zipx", ".7z", ".s7z", ".zz", ".rar", ".tar.gz", ".tgz", ".tar.Z", ".tar.bz2", ".tbz2", ".tar.lzma", ".tlz", ".apk", ".jar", ".dmg", "ipa", "war", "cab", "dar", "aar", "doc", "pdf")

    /* val SUPPORTED_CODE_FILE_EXTENSIONS = Arrays.asList(
             "bsh", "c", "cc", "cpp", "cs", "csh", "cyc", "cv", "htm", "html", "java",
             "js", "m", "mxml", "perl", "pl", "pm", "py", "rb", "sh", "xhtml", "xml",
             "xsl"
     )
*/


    private val GITHUB_BASE_URL_PATTERN_STR = "(https://)?(http://)?(www.)?github.com"

    val REPO_FULL_NAME_PATTERN = Pattern.compile("([a-z]|[A-Z]|\\d|-)*/([a-z]|[A-Z]|\\d|-|\\.|_)*")
    private val USER_PATTERN = Pattern.compile("$GITHUB_BASE_URL_PATTERN_STR/([a-z]|[A-Z]|\\d|-)*(/)?")
    private val REPO_PATTERN = Pattern.compile("$GITHUB_BASE_URL_PATTERN_STR/([a-z]|[A-Z]|\\d|-)*/([a-z]|[A-Z]|\\d|-|\\.|_)*(/)?")
    private val ISSUE_PATTERN = Pattern.compile("$GITHUB_BASE_URL_PATTERN_STR/([a-z]|[A-Z]|\\d|-)*/([a-z]|[A-Z]|\\d|-|\\.|_)*/issues/(\\d)*(/)?")


    fun isUserUrl(url: String): Boolean {
        return USER_PATTERN.matcher(url).matches()
    }

    fun isRepoUrl(url: String): Boolean {
        return REPO_PATTERN.matcher(url).matches()
    }

    fun getUserFromUrl(url0: String): String? {
        var url = url0
        if (!isUserUrl(url)) return null
        if (url.endsWith("/")) url = url.substring(0, url.length - 1)
        return url.substring(url.lastIndexOf("/") + 1)
    }


    fun getRepoFullNameFromUrl(url0: String): String? {
        var url = url0
        if (!isRepoUrl(url)) return null
        if (url.endsWith("/")) url = url.substring(0, url.length - 1)
        return url.substring(url.indexOf("com/") + 4)
    }


    fun isIssueUrl(url: String): Boolean {
        return ISSUE_PATTERN.matcher(url).matches()
    }

    fun isDir(file: File): Boolean {
        return file.type.contentEquals("dir")
    }

    fun isImage(nam: String?): Boolean {
        var name = nam
        if (name!!.isEmpty()) return false
        name = name.trim().toLowerCase()
        for (value in Utils.IMAGE_EXTENSIONS) {
            val extension = MimeTypeMap.getFileExtensionFromUrl(name)
            if (extension != null && value.trim().replace(".", "") == extension || name.endsWith(value))
                return true
        }
        return false
    }

    fun isNotDownloadableFileFormat(nam: String?): Boolean {
        var name = nam
        if (name!!.isEmpty()) return false
        name = name.trim().toLowerCase()
        for (value in ARCHIVE_EXTENSIONS) {
            val extension = MimeTypeMap.getFileExtensionFromUrl(name)
            if (extension != null && value.trim().replace(".", "") == extension || name.endsWith(value))
                return true
        }
        return false
    }

    /*  fun isSupportedCode(name: String?): Boolean {
          var name = name
          if (name!!.isEmpty()) return false
          name = name.toLowerCase()
          for (value in SUPPORTED_CODE_FILE_EXTENSIONS) {
              val extension = MimeTypeMap.getFileExtensionFromUrl(name)
              if (extension != null && value.replace(".", "") == extension || name.endsWith(value))
                  return true
          }

          return false
      }*/
    fun getAbsolutePath(path: String): String {
        var finalText = path
        if (finalText.contains("/")) {
            val lastIndex = finalText.lastIndexOf("/")
            finalText = finalText.substring(lastIndex).removePrefix("/")
        }
        return finalText

    }

    fun getIssuePath(url: String): ArrayList<String> {

        val data = ArrayList<String>(3)

        val url0 = if (url.endsWith("/")) url.substring(0, url.length - 1) else url
        try {
            val uri = Uri.parse(url0)
            val list = ArrayList(uri.pathSegments)
            list.remove("issues")
            if (list.size > 0) data.add(list[0])
            if (list.size > 1) data.add(list[1])
            if (list.size > 2) data.add(list[2])
        } catch (e: Exception) {

        }

        return data

    }


    fun isJson(Json: String): Boolean {
        try {
            JSONObject(Json)
        } catch (ex: JSONException) {
            try {
                JSONArray(Json)
            } catch (ex1: JSONException) {
                return false
            }

        }

        return true
    }

    fun generateHtmlSourceHtml(htmlSource: String, backgroundColor: String,
                               accentColor: String): String {
        return "<html>" +
                "<head>" +
                "<meta charset=\"utf-8\" />\n" +
                "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0;\"/>" +
                "<style>" +
                "body{background: " + backgroundColor + ";}" +
                "a {color:" + accentColor + " !important;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                htmlSource +
                "</body>" +
                "</html>"
    }

}