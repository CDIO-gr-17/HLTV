package com.example.hltv.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertTimestampToDateDisplay(timestamp: Int?): String {
    val dateFormat = DateFormat.getDateInstance()
    dateFormat.timeZone = TimeZone.getTimeZone("CET")
    if(timestamp != null) {
        val date = Date(timestamp.toLong()*1000L)// Assuming the timestamp is in seconds, multiply by 1000 for milliseconds
        return dateFormat.format(date)
    } else {
        return "Unknown date"
    }
}

fun convertTimestampToDateURL(timestamp: Int?): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("CET")
    if (timestamp != null) {
        val date =
            Date(timestamp.toLong() * 1000) // Assuming the timestamp is in seconds, multiply by 1000 for milliseconds
        return dateFormat.format(date)
    } else {
        return "Unknown date"
    }
}

fun convertTimestampToDateClock(timestamp: Int?): String {
    val dateFormat = SimpleDateFormat("EEEE, dd/MM HH:mm", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("CET")
    if (timestamp != null) {
        val date = Date(timestamp.toLong() * 1000) // Assuming the timestamp is in seconds, multiply by 1000 for milliseconds
        return dateFormat.format(date)
    } else {
        return "Unknown date"
    }
}

private fun extractYearFromString(input: String): String? {
    val pattern = "\\d{4}".toRegex()
    val match = pattern.find(input)
    return match?.value
}

fun convertYearToUnixTimestamp(stringWithYear: String): Int {
    val yearString = extractYearFromString(stringWithYear)
    if (yearString != null) {
        val year = yearString.toInt()
        val date = LocalDate.of(year, 7, 1) // Middle of the year
        val zoneId = ZoneId.systemDefault()
        return date.atStartOfDay(zoneId).toEpochSecond().toInt()
    }else{
        throw NumberFormatException("String doesn't contain a year")
    }

}

fun countdownTimer(timeStamp: Int?): Int {
    val currentTimeMillis = System.currentTimeMillis()
    if(timeStamp!=null) {
        var timeLeft = (timeStamp.toLong() - currentTimeMillis) / 1000

        runBlocking {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
        }

        return timeLeft.toInt()
    }
    else{
        return 0
    }
}
