package com.jk.gogit.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.core.graphics.ColorUtils
import com.jk.gogit.R
import com.jk.gogit.extensions.loading
import kotlinx.android.synthetic.main.image_layout.*


object DialogUtils {
    fun showImageDialog(context: Context, name: String?, url: String, color: Int) {
        val settingsDialog = Dialog(context)
        with(settingsDialog) {
            settingsDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            settingsDialog.setContentView(R.layout.image_layout)
            dialogImage.apply {
                loading(url)
                txt_name_d.apply {
                    setBackgroundColor(ColorUtils.setAlphaComponent(color, 90))
                    setTextColor(ViewUtils.getLabelTextColor(context, color))
                    text = name

                }
            }
        }
        settingsDialog.show()
    }



}