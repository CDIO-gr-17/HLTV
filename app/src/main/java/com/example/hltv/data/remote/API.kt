package com.example.hltv.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.IOException

const val APIKEY = "24b0f292d5mshdf7eb12b4760333p19075ajsncc1561769190"
var MILISBETWEENREQUESTS: Long = 200
var CURRENTMILISBETWEENREQUEST: Long = MILISBETWEENREQUESTS //If this is set to 167 then some images disappear, not sure why. Maybe API counts time from when it stopped sending the last request?

const val ONLYCS = true
var lastAPIPull: Long = 0
val mutexForAPI = Mutex()
suspend fun waitForAPI() {
    mutexForAPI.withLock {
        val delta = ((lastAPIPull + CURRENTMILISBETWEENREQUEST) - java.util.Date().time)
        delay(delta)
        lastAPIPull = java.util.Date().time
    }
}

/**
 * Returns live matches
 */
suspend fun getLiveMatches(): APIResponse.EventsWrapper {
    val eventsWrapper = getAPIResponse(
        "matches/live",
        APIKEY,
        APIResponse.EventsWrapper::class.java
    ) as APIResponse.EventsWrapper
    if (ONLYCS) {
        val csEvents: MutableList<Event> = mutableListOf()
        for (event in eventsWrapper.events) {
            if (event.tournament.category?.name.equals("Counter Strike")) {
                csEvents.add(event)
            }
        }
        eventsWrapper.events = csEvents
    }
    return eventsWrapper
}

/**
 * @return 2x5 players. I don't know what "confirmed" means
 */
suspend fun getPlayersFromEvent(eventID: Int? = 10945127): APIResponse.Lineup {
    print(eventID)
    return getAPIResponse(
        "event/" + eventID.toString() + "/lineups",
        APIKEY,
        APIResponse.Lineup::class.java,
        1
    ) as APIResponse.Lineup
}

suspend fun searchInAPIFromString(searchQuery: String): APIResponse.ResultsWrapper {
    return try {
        getAPIResponse(
            "search/$searchQuery",
            APIKEY,
            APIResponse.ResultsWrapper::class.java
        ) as APIResponse.ResultsWrapper

    } catch (e: Exception) {
        // handling empty response
        Log.e("searchInAPIFromString", "Exception: $e")
        APIResponse.ResultsWrapper(emptyList())
    }

}


suspend fun getPlayerFromPlayerID(playerID: Int? = 1078255): APIResponse.PlayerWrapper {
    return getAPIResponse(
        "player/" + playerID.toString(),
        APIKEY,
        APIResponse.PlayerWrapper::class.java
    ) as APIResponse.PlayerWrapper
}


//Doesn't use the reusable function because of the return type
suspend fun getPlayerImage(playerID: Int? = 1078255): Bitmap? {
    Log.v("getPlayerImage", "Getting player image with playerID " + playerID.toString())
    val apiURL = "player/" + playerID.toString() + "/image"
    return getAPIImage(apiURL, APIKEY)
}

suspend fun getTeamImage(teamID: Int? = 372647): Bitmap? {
    val apiURL = "team/" + teamID.toString() + "/image"
    return getAPIImage(apiURL, APIKEY)
}

suspend fun getPreviousMatches(teamID: Int, pageID: Int = 0): APIResponse.EventsWrapper {

    return getAPIResponse(
        "team/$teamID/matches/previous/$pageID",
        APIKEY,
        APIResponse.EventsWrapper::class.java
    ) as APIResponse.EventsWrapper
}

suspend fun getGamesFromEvent(eventID: Int?): APIResponse.GameWrapper {
    return try {
        getAPIResponse(
            "event/$eventID/games",
            APIKEY,
            APIResponse.GameWrapper::class.java
        ) as APIResponse.GameWrapper
    } catch (e: Exception) {
        Log.e("getGamesFromEvent", "No games found for match $eventID")
        APIResponse.GameWrapper(emptyList())
    }
}

suspend fun getMapImageFromMapID(mapID: Int): Bitmap? {
    val apiURL = "map/$mapID/image"
    return getAPIImage(apiURL, APIKEY)

}

suspend fun getEvent(eventID: Int?): APIResponse.EventWrapper {
    return getAPIResponse(
        "event/$eventID",
        APIKEY,
        APIResponse.EventWrapper::class.java
    ) as APIResponse.EventWrapper
}

/**
 * I couldn't get coil to work with the whole APIkey, MVVM model and stuff
 * If you can, feel free to, but this slightly convoluted thing works
 */
private suspend fun getAPIImage(apiURL: String, apiKEY: String): Bitmap? {

    Log.i(
        "getAPIImage",
        "Attempting to get: https://allsportsapi2.p.rapidapi.com/api/esport/$apiURL"
    )

    val request = Request.Builder()
        .url("https://allsportsapi2.p.rapidapi.com/api/esport/$apiURL")
        .get()
        .addHeader("X-RapidAPI-Key", apiKEY)
        .addHeader("X-RapidAPI-Host", "allsportsapi2.p.rapidapi.com")
        .build()

    val client = OkHttpClientSingleton.instance
    waitForAPI()
    val response = client.newCall(request).execute()


    val inputStream2 = response.body?.byteStream()
    val buffer = ByteArray(1024)
    val output = ByteArrayOutputStream()

    if (inputStream2 != null) {
        var bytesRead = inputStream2.read(buffer) //Not really a problem since it already called from coroutineScope
        while (bytesRead != -1) {
            output.write(buffer, 0, bytesRead)
            bytesRead = inputStream2.read(buffer) //Not really a problem since it already called from coroutineScope
        }
    } else {
        Log.i("getAPIImage", "inputStream2 is null")
    }

    val responseBodyCopy = response.body
    val jsonString = responseBodyCopy?.string()
    checkRequestRate(jsonString)

    val base64String = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)
    val decodedImage: ByteArray = Base64.decode(base64String, 0)
    val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)

    if (bitmap == null) {
        Log.d("getAPIImage", "Bitmap is null, probably because player image does not exist")
    }
    return bitmap

}

suspend fun getTeamNameFromID(teamID: Int): String? {
    val team = getAPIResponse(
        "team/$teamID",
        APIKEY,
        APIResponse.TeamContainer::class.java
    ) as APIResponse.TeamContainer
    return if (team.team.name != null) team.team.name.toString()
    else null
}
/**
 * @return The API's ID of Counter-Strike
 */
suspend fun getCSCategory(): Int {
    val categoryWrapper = getAPIResponse(
        "tournament/categories",
        APIKEY,
        APIResponse.CategoryWrapper::class.java
    ) as APIResponse.CategoryWrapper
    var categories = 0
    for (category in categoryWrapper.categories) {
        if (category.slug.equals("csgo")) {
            categories = category.id!!
        }
    }
    return categories
}

/**
 * @param catID The API's ID of Counter-Strike
 * @return A list of relevant tournament IDs (above 1000 users)
 */
suspend fun getCSTournamentsID(catID: Int): List<Int> {
    val acceptableUserCount = 0
    val tournamentWrapper = getAPIResponse(
        "tournament/all/category/$catID",
        APIKEY,
        APIResponse.TournamentWrapper::class.java
    ) as APIResponse.TournamentWrapper
    val tournamentIDs: MutableList<Int> = mutableListOf()
    var i  = 0
    for (tournament in tournamentWrapper.uniqueTournament[0].wrapper) {
        if (tournament.userCount!! >= acceptableUserCount) {
            tournamentIDs.add(tournament.id!!)
            i++
        }
    }
    Log.d("Number of Tournaments gone through", i.toString())
    return tournamentIDs
}
/**
 * @param tournamentID The ID of the tournament to get info from
 * @return A wrapper class containing the tournament details
 */
suspend fun getTournamentInfo(tournamentID: Int): APIResponse.ThirdTournamentWrapper {
    return getAPIResponse(
        "tournament/$tournamentID",
        APIKEY,
        APIResponse.ThirdTournamentWrapper::class.java
    ) as APIResponse.ThirdTournamentWrapper
}

suspend fun getRelevantTournaments(): List<ThirdUniqueTournament> {
    var finalTournamentDetailList: MutableList<ThirdUniqueTournament> = mutableListOf()

    val tournaments: List<Int> = getCSTournamentsID(getCSCategory())

    val croppedTournaments = tournaments.take(15)

    val deferreds = croppedTournaments.map { tournamentID ->
        CoroutineScope(Dispatchers.IO).async {
            getTournamentInfo(tournamentID).tournamentDetails
        }
    }

    finalTournamentDetailList.addAll(deferreds.awaitAll())
    finalTournamentDetailList.sortBy { it.startDateTimestamp }
    finalTournamentDetailList = finalTournamentDetailList.reversed().toMutableList()
    return finalTournamentDetailList
}

suspend fun getMatchesFromDay(timestamp: String): APIResponse.EventsWrapper {
    val matchesFromDay = getAPIResponse(
        "category/1572/events/$timestamp",
        APIKEY,
        APIResponse.EventsWrapper::class.java
    ) as APIResponse.EventsWrapper
    if (matchesFromDay.events.isEmpty()) {
        Log.i("getMatchesFromDay", "There are no more matches to load")
    }
    matchesFromDay.events =
        matchesFromDay.events.subList(0, (maxOf(matchesFromDay.events.size - 1, 0)))
    return matchesFromDay
}

suspend fun getTournamentLogo(tournamentID: Int? = 16026): Bitmap? {
    val apiURL = "tournament/" + tournamentID.toString() + "/image"
    Log.i("tournamentLogo", "Getting tournamentlogo with URL: $apiURL")
    return getAPIImage(apiURL, APIKEY)
}


suspend fun getUniqueTournamentDetails(
    tournamentID: Int? = 16026,
    seasonID: Int? = 47832
): APIResponse.UniqueTournamentInfoWrapper {
    return getAPIResponse(
        "tournament/${tournamentID.toString()}/season/${seasonID.toString()}/info", APIKEY,
        APIResponse.UniqueTournamentInfoWrapper::class.java
    ) as APIResponse.UniqueTournamentInfoWrapper
}

suspend fun getUniqueTournamentSeasons(tournamentID: Int? = 16137): APIResponse.SeasonsWrapper {
    return getAPIResponse(
        "tournament/${tournamentID.toString()}/seasons",
        APIKEY,
        APIResponse.SeasonsWrapper::class.java
    ) as APIResponse.SeasonsWrapper
}

suspend fun getTournamentStandings(tournamentID: Int? = 16137, seasonID: Int? = 51868): APIResponse.StandingsWrapper {
    val apiURL = "tournament/$tournamentID/season/$seasonID/standings/total"
    return try {
        getAPIResponse(
            apiURL,
            APIKEY,
            APIResponse.StandingsWrapper::class.java
        ) as APIResponse.StandingsWrapper
    } catch (e: Exception){
        Log.e("tournamentStandings","No tournament standings found for tournamentID $tournamentID, seasonID $seasonID")
        APIResponse.StandingsWrapper(arrayListOf())
    }
}


suspend fun checkRequestRate(jsonString: String?): Boolean {
    if (jsonString != null) {
        if (jsonString.contains("You have exceeded the rate limit per second for your plan")) {
            Log.e(
                "getAPIResponse",
                "You have exceeded the rate limit per second for your plan. Allowing less requests/second to allow for multiple app instances"
            )
            CURRENTMILISBETWEENREQUEST += MILISBETWEENREQUESTS
            delay(CURRENTMILISBETWEENREQUEST)
            return true
        }
    }
    return false
}

/**
 * @param desiredClass The class to pass to gson. This is the same as your return class, e.g. APIResponse.Lineup::class.java
 */

private suspend fun getAPIResponse(
    apiURL: String,
    apiKEY: String,
    desiredClass: Class<*>,
    tries : Int = 3
): APIResponse {

    var jsonString: String?
    var tries = tries
    var apiInUse: Boolean
    val gson = GsonSingleton.instance

    do {
        Log.i(
            "getAPIResponse",
            "Attempting to get: https://allsportsapi2.p.rapidapi.com/api/esport/$apiURL"
        )
        val request = Request.Builder()
            .url("https://allsportsapi2.p.rapidapi.com/api/esport/$apiURL")
            .get()
            .addHeader("X-RapidAPI-Key", apiKEY)
            .addHeader("X-RapidAPI-Host", "allsportsapi2.p.rapidapi.com")
            .build()

        val client = OkHttpClientSingleton.instance
        waitForAPI()
        val response = client.newCall(request).execute()
        // Get the HTTP response as a string
        jsonString = response.body?.string()
        apiInUse = checkRequestRate(jsonString)
        response.close()

        if (jsonString != null) {
            if (jsonString.contains("You have exceeded the rate limit per second for your plan")) {
                Log.e("getAPIResponse", "You have exceeded the rate limit per second for your plan")
                apiInUse = true
            }
        } else {
            Log.i("getAPIResponse", "jsonString is null")
        }
        tries--
    } while (jsonString?.compareTo("") == 0 && tries > 0 || apiInUse)

    if (jsonString?.compareTo("") == 0) {
        Log.e("getAPIResponse", "jsonString is repeatedly null", IOException("STRING IS NULL"))
        return APIResponse.Error //tried this
    }

    if (jsonString != null) {
        if (jsonString.length > 100) {
            Log.i("getAPIResponse", "JSON IS: " + jsonString.substring(0, 100) + "...")
        } else {
            Log.i("getAPIResponse", "JSON IS: $jsonString")

        }
    }


    return gson.fromJson(jsonString, desiredClass) as APIResponse
}

/* NOt needed anymore we are using teamMedia
suspend fun getTournamentMedia(uniqueTournamentID: String): APIResponse.MediaWrapper{
    try {
        return getAPIResponse(
            "tournament/$uniqueTournamentID/media",
            APIKEY,
            APIResponse.MediaWrapper::class.java
        ) as APIResponse.MediaWrapper
    }
    catch (e: Exception){
        Log.e("getTournamentMedia()", "$e")
        return APIResponse.MediaWrapper(Media())
    }
}*/
suspend fun getTeamMedia(teamID: Int?): APIResponse.MediaWrapper {
    return try {
        getAPIResponse(
            "team/$teamID/media",
            APIKEY,
            APIResponse.MediaWrapper::class.java
        ) as APIResponse.MediaWrapper
    } catch (e: Exception) {
        //handling when response is empty
        Log.e("getTeamMedia()", "$e")
        APIResponse.MediaWrapper(ArrayList())
    }

}


fun main() {


}