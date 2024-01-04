package com.example.hltv.data

import java.text.DateFormat
import java.text.SimpleDateFormat
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