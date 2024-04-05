package com.jk.gogit.extensions

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