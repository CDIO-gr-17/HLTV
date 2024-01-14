package com.example.hltv.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
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

fun convertTimestampToWeekDateClock(timestamp: Int?): String {
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

class CountdownViewModel : ViewModel() {
    private val _remainingTime = MutableStateFlow<Long?>(null)
    val remainingTime = _remainingTime.asStateFlow()

    private var countdownJob: Job? = null
    fun startCountdown(targetTimestamp: Int) {
        countdownJob?.cancel()

        countdownJob = viewModelScope.launch {
            while (System.currentTimeMillis()/1000 < targetTimestamp) {
                val remainingMillis = targetTimestamp - System.currentTimeMillis()/1000
                _remainingTime.value = remainingMillis
                delay(1000)
            }
            _remainingTime.value = 0 // Countdown reached, set remaining time to 0
        }
    }
    fun stopCountdown() {
        _remainingTime.value = null
        countdownJob?.cancel()
    }
    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}


fun convertTimestampToDateClock(timestamp: Int?): String {
    val dateFormat = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("CET")
    if (timestamp != null) {
        val date = Date(timestamp.toLong() * 1000) // Assuming the timestamp is in seconds, multiply by 1000 for milliseconds
        return dateFormat.format(date)
    } else {
        return "Unknown date"
    }
}
fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

fun getAvgAgeFromTimestamp(dateOfBirthTimestampList: MutableList<Int>): Double {            //TODO: This should be moved to a more appropriate place
    var totalAgeOfPlayers: Long = 0
    for (dateOfBirthTimestamp in dateOfBirthTimestampList) {
        totalAgeOfPlayers += ((System.currentTimeMillis() // Subtracts the current time in milliseconds from the players date of birth in milliseconds
                - (dateOfBirthTimestamp.toLong() * 1000)))
    }
    if(dateOfBirthTimestampList.size!=0) {
        val avgAgeOfPlayersInMillis: Long = totalAgeOfPlayers / dateOfBirthTimestampList.size
        val df = DecimalFormat("#.#")
        val avgAgeOfPlayersInYears = avgAgeOfPlayersInMillis/365.25/3600/24/1000
        df.roundingMode = RoundingMode.CEILING
        print(avgAgeOfPlayersInYears.toDouble())
        return avgAgeOfPlayersInYears.toDouble()

        // String.format("%.1f", TimeUnit.MILLISECONDS.toDays(avgAgeOfPlayersInMillis) / 365.25).toDouble()

    }
    else return 0.0
}

