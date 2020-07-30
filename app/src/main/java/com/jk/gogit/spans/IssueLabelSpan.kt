package com.jk.gogit.spans

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

import com.jk.gogit.R
import com.jk.gogit.model.Issue
import com.jk.gogit.utils.ViewUtils


class IssueLabelSpan(context: Context, label: Issue.Label) : ReplacementSpan() {

    private val textColor: Int
    private val bgColor: Int
    private val padding: Float
    private val margin: Float
    private val bgRadius: Float

    private var ascent: Int = 0
    private var descent: Int = 0

    init {
        bgColor = Color.parseColor("#" + label.color)
        textColor = ViewUtils.getLabelTextColor(context, bgColor)
        padding = context.resources.getDimension(R.dimen.dp5)
        margin = context.resources.getDimension(R.dimen.dp5)
        bgRadius = context.resources.getDimension(R.dimen.dp5)
    }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int,
                         fm: Paint.FontMetricsInt?): Int {
        if (fm != null) {
            paint.getFontMetricsInt(fm)
            ascent = -fm.ascent
            descent = fm.descent
            fm.top -= (padding + margin).toInt()
            fm.ascent -= (padding + margin).toInt()
            fm.bottom += (padding + margin).toInt()
            fm.descent += (padding + margin).toInt()
        }

        val textWidth = Math.ceil(paint.measureText(text, start, end).toDouble()).toInt()
        return (textWidth + 2 * (padding + margin)).toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float,
                      top: Int, y: Int, bottom: Int, paint: Paint) {
        val textSize = paint.measureText(text, start, end)

        val bgLeft = x + margin
        val bgRight = bgLeft + textSize + 2 * padding
        val bgTop = y.toFloat() - ascent.toFloat() - padding
        val bgBottom = y.toFloat() + descent.toFloat() + padding
        val rectF = RectF(bgLeft, bgTop, bgRight, bgBottom)

        paint.color = bgColor
        canvas.drawRoundRect(rectF, bgRadius, bgRadius, paint)

        paint.color = textColor
        canvas.drawText(text, start, end, x + padding + margin, y.toFloat(), paint)

    }

}
