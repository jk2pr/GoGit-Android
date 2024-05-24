package com.jk.gogit.extensions

import androidx.compose.ui.graphics.Color
import org.joda.time.DateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

fun String.toDate():Date {
    val dateTime = DateTime.parse(replace(" ", "T"))
   return dateTime.toLocalDate().toDate()
}
 fun Date.formatDateRelativeToToday(): String {
    val dateInstant = toInstant()
    val localDateTime = LocalDateTime.ofInstant(dateInstant, ZoneId.systemDefault())
    val localDate = localDateTime.toLocalDate()

    val today = LocalDate.now()
    val daysDifference = ChronoUnit.DAYS.between(localDate, today)

    return when {
        daysDifference == 0L -> "Today"
        daysDifference == 1L -> "Yesterday"
        daysDifference < 7L -> "$daysDifference days ago"
        else -> localDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
}
fun Int.formatNumber(): String {
    return when {
        this >= 1_000_000_000 -> "${(this / 1_000_000_000.toFloat()).format()}B"
        this >= 1_000_000 -> "${(this / 1_000_000.toFloat()).format()}M"
        this >= 1_000 -> "${(this / 1_000.toFloat()).format()}K"
        else -> this.toString()
    }
}

fun Float.format(): String {
    return if (this == this.toInt().toFloat()) {
        this.toInt().toString()
    } else {
        "%.1f".format(this)
    }
}
fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}

    /*return when {
        this >= 1000000 -> (this / 1000000).toDouble().roundToInt().toString() + "M"
        this >= 1000 -> (this / 1000).toDouble().roundToInt().toString() + "k"
        else -> this.toString()
    }*/
