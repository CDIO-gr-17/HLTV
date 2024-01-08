package com.example.hltv.data.remote
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.ConditionVariable
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
const val MILISBETWEENREQUEST : Long = 200 //If this is set to 167 then some images disappear,
// not sure why. Maybe API counts time from when it stopped sending the last request?
const val ONLYCS = true
var currentRequestCount = 0
val cond = ConditionVariable()
var lastAPIPull: Long = 0
val mutexForAPI = Mutex()
var totalSaved = 0.0
suspend fun waitForAPI(){


    mutexForAPI.withLock {

        //Mixing these two seemed to break it, so fix that
        val delta = ((lastAPIPull + MILISBETWEENREQUEST) - java.util.Date().time)
        delay(delta)
        lastAPIPull = java.util.Date().time

        /*
        val saved = minOf(MILISBETWEENREQUEST - delta, MILISBETWEENREQUEST)
        totalSaved += saved

        Log.i(
            "waitForAPI",
            "New wait implementation saved: " + saved.toString() + "ms, in total " + totalSaved.toString() + "ms"
        )
        */

    }
}

/**
 * Returns live matches
 */
suspend fun getLiveMatches(): APIResponse.EventsWrapper {


    val eventsWrapper = getAPIResponse("matches/live", APIKEY, APIResponse.EventsWrapper::class.java) as APIResponse.EventsWrapper
    if (ONLYCS){
        val csEvents: MutableList<Event> = mutableListOf()
        for (event in eventsWrapper.events){//We should also be able to use slug or flag instead of name
            if(event.tournament?.category?.name.equals("Counter Strike")){
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
    return getAPIResponse("event/" + eventID.toString() + "/lineups", APIKEY, APIResponse.Lineup::class.java) as APIResponse.Lineup
}
suspend fun searchInAPIFromString(searchQuery : String) : APIResponse.resultsWrapper {
    try {
        return getAPIResponse("search/$searchQuery", APIKEY, APIResponse.resultsWrapper::class.java) as APIResponse.resultsWrapper

    } catch (e: Exception) {
        // handling empty response
        Log.e("searchInAPIFromString", "Exception: $e")
        return APIResponse.resultsWrapper(emptyList())
    }

}

//This is what we call a "fyfy"
//const val playerSilhouetteAsBase64: String = "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"
suspend fun getPlayerImage(playerID: Int? = 1078255): Bitmap? {
    Log.v("getPlayerImage", "Getting player image with playerID " + playerID.toString())
    val apiURL = "player/" + playerID.toString() + "/image"
    var image = getAPIImage(apiURL, APIKEY)
    /*
    if (image == null){
        val decodedImage: ByteArray = Base64.decode(playerSilhouetteAsBase64, 0)
        image = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)
    }*/
    return image
}

suspend fun getTeamImage(teamID: Int? = 372647): Bitmap? {
    val apiURL = "team/" + teamID.toString() + "/image"
    return getAPIImage(apiURL, APIKEY)
}

suspend fun getPreviousMatches(teamID: Int, pageID: Int = 0):APIResponse.EventsWrapper{
    return getAPIResponse("team/"+teamID.toString()+"/matches/previous/"+ pageID, APIKEY, APIResponse.EventsWrapper::class.java) as APIResponse.EventsWrapper
}

suspend fun getGamesFromEvent(eventID: Int?) : APIResponse.EventsWrapper{
    return getAPIResponse("event/$eventID/games", APIKEY, APIResponse.EventsWrapper::class.java) as APIResponse.EventsWrapper
}
suspend fun getEvent(eventID: Int?) : APIResponse.EventWrapper{
    return getAPIResponse("event/$eventID", APIKEY, APIResponse.EventWrapper::class.java) as APIResponse.EventWrapper
}
/**
 * I couldn't get coil to work with the whole APIkey, MVVM model and stuff
 * If you can, feel free to, but this slightly convoluted thing works
 */
private suspend fun getAPIImage(apiURL: String, apiKEY: String): Bitmap?{

    Log.i("getAPIResponse",
        "Attempting to get: https://allsportsapi2.p.rapidapi.com/api/esport/$apiURL"
    )

    val request = Request.Builder()
        .url("https://allsportsapi2.p.rapidapi.com/api/esport/" + apiURL)
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
        var bytesRead = inputStream2.read(buffer)
        while (bytesRead != -1) {
            output.write(buffer, 0, bytesRead)
            bytesRead = inputStream2.read(buffer)
        }
    }else{
        Log.i("getAPIImage", "inputStream2 is null")
    }

    //TODO: This is unnecessary
    val base64String = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)
    val decodedImage: ByteArray = Base64.decode(base64String, 0)
    val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)

    if (bitmap == null){
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

    //Had a nullpointerexception here
    //val name = .name
    if (team.team != null && team.team.name != null) return team.team.name.toString()
    else return null
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

/**
 * @return A list of tournaments that has a user count of over 1000
 * Dont know what user count means. Can be adjusted in @getCSTournamentsID
 */
suspend fun getRelevantTournaments(): List<ThirdUniqueTournament> {
    val finalTournamentDetailList: MutableList<ThirdUniqueTournament> = mutableListOf()
    //val tempTournamentDetailList: MutableList<ThirdUniqueTournament> = mutableListOf() //TODO This is commented out for performance reasons

    //By doing this we make sure that all requests are sent as fast as possible,
    // rather than sending one, waiting for a reply and then sending another, waiting for a reply...
    val deferreds = getCSTournamentsID(getCSCategory()).map { tournamentID ->
        CoroutineScope(Dispatchers.IO).async {
            getTournamentInfo(tournamentID).tournamentDetails
        }
    }

    finalTournamentDetailList.addAll(deferreds.awaitAll())
    finalTournamentDetailList.sortBy { it.startDateTimestamp }
    return finalTournamentDetailList
}
suspend fun getMatchesFromDay(timestamp: String): APIResponse.EventsWrapper {
    val matchesFromDay = getAPIResponse(
        "category/1572/events/$timestamp",
        APIKEY,
        APIResponse.EventsWrapper::class.java
    ) as APIResponse.EventsWrapper
    /*
    if (matchesFromDay.events.size == 0){
        Log.i("getMatchesFromDay","There are no more matches to load")
    }

     */
    matchesFromDay.events = matchesFromDay.events.subList(0,(maxOf(matchesFromDay.events.size-1,0)))
    return matchesFromDay
}
suspend fun getTournamentLogo(tournamentID: Int? = 16026): Bitmap?{
    val apiURL = "tournament/" + tournamentID.toString() + "/image"
    //Log.i("tournamentLogo", "Getting tournamentlogo with URL: $apiURL")
    return getAPIImage(apiURL, APIKEY)
}
/*
private fun checkIfTournamentIsPast(timeStamp: TimeStamp): Boolean{

    return false
}
*/

/**
 * @param desiredClass The class to pass to gson. This is the same as your return class, e.g. APIResponse.Lineup::class.java
 */

private suspend fun getAPIResponse(apiURL: String, apiKEY: String, desiredClass:Class<*>): APIResponse {

    var jsonString : String?
    var tries = 3
    var apiInUse = false
    Log.i("getAPIResponse",
        "Attempting to get: https://allsportsapi2.p.rapidapi.com/api/esport/$apiURL"
    )
    do{
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
        response.close()

        if (jsonString != null) {
            if (jsonString.contains("You have exceeded the rate limit per second for your plan")){
                Log.e("getAPIResponse", "You have exceeded the rate limit per second for your plan")
                apiInUse = true
            }
        }else {
            Log.i("getAPIResponse", "jsonString is null")
        }
        tries--
    }while (jsonString?.compareTo("") == 0 && tries > 0 && !apiInUse)

    if (jsonString?.compareTo("") == 0){
        Log.e("getAPIResponse", "jsonString is repeatedly null", IOException("STRING IS NULL"))
    }

    if (jsonString != null) {
        if (jsonString.length > 100){
            Log.i("getAPIResponse", "JSON IS: " + jsonString.substring(0,100) + "...")
        } else{
            Log.i("getAPIResponse", "JSON IS: " + jsonString)

        }
    }



    //Initiating as late as possible for performance reasons. Don't think it makes much of a difference
    val gson = GsonSingleton.instance
    return gson.fromJson(jsonString, desiredClass) as APIResponse
}

fun main() {

    //val a = getPreviousMatches(364425,0)
    //val b = getLiveMatches()
    //val c = getPlayersFromEvent(b?.events?.get(0)?.id)
    //val d = getPlayerImage()

}