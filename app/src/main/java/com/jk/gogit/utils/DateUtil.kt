package com.jk.gogit.utils

import android.text.format.DateUtils
import android.text.format.DateUtils.DAY_IN_MILLIS
import org.joda.time.DateTime
import java.util.*


object DateUtil {

    private fun getLocalDateFromString(time: String): Date {

        val dateTime = DateTime.parse(time.replace(" ", "T"))
        return dateTime.toLocalDate().toDate()

    }

    fun getDateComparatively(time: String): String {
        val createdDate = getLocalDateFromString(time)
        val now = System.currentTimeMillis()
        val charSequence = DateUtils.getRelativeTimeSpanString(createdDate.time, now, DAY_IN_MILLIS).toString()

        return if (!charSequence.toLowerCase().contains("days ago")
                || !charSequence.toLowerCase().contains("Today")
                || !charSequence.toLowerCase().contains("Yesterday"))
            charSequence
        else
            "on $charSequence"
    }
}
