package com.example.hltv.data.remote
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.ConditionVariable
import android.util.Base64
import android.util.Log
import com.example.hltv.data.local.avgStatsOfTeam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.IOException

const val APIKEY = "24b0f292d5mshdf7eb12b4760333p19075ajsncc1561769190"
const val ONLYCS = true
var currentRequestCount = 0
val cond = ConditionVariable()
var lastAPIPull: Long = 0
val mutexForAPI = Mutex()

suspend fun waitForAPI(){

    //TODO: This needs to be optimized because it assumes that all other operations take 0 time. Set to 200 because 166 sometimes gives errors
    mutexForAPI.withLock {

        delay(200)
    }

    /*
    val currentDateTime: java.util.Date = java.util.Date()
    val currentTimestamp = currentDateTime.time
    val nextAllowedTime = lastAPIPull + 166;
    val delta = nextAllowedTime-currentTimestamp
    delay(delta)
    Log.i("waitForAPI", "I am being called")

     */
/*
    if (currentRequestCount < 6){
        currentRequestCount += 1
        run{
            delay(166)
            currentRequestCount -=1
            cond.open()
        }
        return
    } else {
        cond.block()
    }

 */
}
suspend fun getMatchStatistics(gameID : Int): APIResponse.LineupStatistics{
    Log.i("getMatchStatistics", "${getAPIResponse("game/$gameID/lineups", APIKEY, APIResponse.LineupStatistics::class.java) as APIResponse.LineupStatistics}")
    return getAPIResponse("game/$gameID/lineups", APIKEY, APIResponse.LineupStatistics::class.java) as APIResponse.LineupStatistics
}

suspend fun getPlayerStatisticsFromEvent(gameID: Int, playerID: Int): APIResponse.PlayerAndStatistics? {
    var playerStats : APIResponse.PlayerAndStatistics ?= null
    var i = 0
    for(item in getMatchStatistics(gameID).homeTeamPlayers){
        if(item.player?.id == playerID)
            playerStats = getMatchStatistics(gameID).homeTeamPlayers[i]
        i++
    }
    i = 0
    for(item in getMatchStatistics(gameID).awayTeamPlayers){
        if(item.player?.id == playerID)
            playerStats = getMatchStatistics(gameID).awayTeamPlayers[i]
        i++
    }
    return playerStats
}
suspend fun getAvgStatsFromTeam(teamID: Int) : avgStatsOfTeam?{
    Log.i("getAvgStatsFromTeam","Started")
    var playerStats : ArrayList<APIResponse.PlayerAndStatistics> ?= null
    var adrList : MutableList<Double> = mutableListOf()
    var assistsList : MutableList<Int> = mutableListOf()
    var deathsList : MutableList<Int> = mutableListOf()
    var firstKillsDiffList : MutableList<Int> = mutableListOf()
    var flashAssistsList : MutableList<Int> = mutableListOf()
    var headshotsList : MutableList<Int> = mutableListOf()
    var kdDiffList : MutableList<Int> = mutableListOf()
    var kastList : MutableList<Int> = mutableListOf()
    var killsList : MutableList<Int> = mutableListOf()

    for(events in getPreviousMatches(teamID,0).events){
        if(events.id!=null && events.homeTeam.id==teamID)
            playerStats = getMatchStatistics(events.id!!).homeTeamPlayers
        //Passing an eventID to getMatchStatistics, but it needs a gameID.
        //Need to implement E-Sports Event Games from the API, to iterate over games in an event
        if (events.id!=null && events.awayTeam.id==teamID)
            playerStats = getMatchStatistics(events.id!!).awayTeamPlayers // Same here
        Log.i("Stats", "playerStats is $playerStats")
        if (playerStats!=null)
            for (player in playerStats){
                if(player.adr!=null && player.adr!= 0.0)
                    adrList.add(player.adr!!)
                Log.i("ADR", "Added adr ${player.adr} of ${player.player?.name}")
                if(player.assists!=null && player.assists!= 0)
                    assistsList.add(player.assists!!)
                if(player.deaths!=null && player.deaths!= 0)
                    deathsList.add(player.deaths!!)
                if(player.firstKillsDiff!=null && player.firstKillsDiff!= 0)
                    firstKillsDiffList.add(player.firstKillsDiff!!)
                if(player.flashAssists!=null && player.flashAssists!= 0)
                    flashAssistsList.add(player.flashAssists!!)
                if(player.headshots!=null && player.headshots!= 0)
                    headshotsList.add(player.headshots!!)
                if(player.kdDiff!=null && player.kdDiff!= 0)
                    kdDiffList.add(player.kdDiff!!)
                if(player.kast!=null && player.kast!= 0)
                    kastList.add(player.kast!!)
                if(player.kills!=null && player.kills!= 0)
                    killsList.add(player.kills!!)
            }
    }
    return avgStatsOfTeam(
        avgAdr = if (adrList.isNotEmpty()) adrList.sum() / adrList.size else 0.0,
        avgAssists = if (assistsList.isNotEmpty()) assistsList.sum() / assistsList.size else 0,
        avgDeaths = if (deathsList.isNotEmpty()) deathsList.sum() / deathsList.size else 0,
        avgFirstKillsDiff = if (firstKillsDiffList.isNotEmpty()) firstKillsDiffList.sum() / firstKillsDiffList.size else 0,
        avgFlashAssists = if (flashAssistsList.isNotEmpty()) flashAssistsList.sum() / flashAssistsList.size else 0,
        avgHeadshots = if (headshotsList.isNotEmpty()) headshotsList.sum() / headshotsList.size else 0,
        avgKdDiff = if (kdDiffList.isNotEmpty()) kdDiffList.sum() / kdDiffList.size else 0,
        avgKast = if (kastList.isNotEmpty()) kastList.sum() / kastList.size else 0,
        avgKills = if (killsList.isNotEmpty()) killsList.sum() / killsList.size else 0
    )
}
/**
 * Returns live matches
 */
suspend fun getLiveMatches(): APIResponse.EventsWrapper {


    val eventsWrapper = getAPIResponse("matches/live", APIKEY, APIResponse.EventsWrapper::class.java) as APIResponse.EventsWrapper
    if (ONLYCS){
        val csEvents: MutableList<Event> = mutableListOf()
        for (event in eventsWrapper.events){//We should also be able to use slug or flag instead of name
            if(event.tournament?.category?.name.equals("CS:GO")){
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



//Doesnt use the reusable function because of the return type
suspend fun getPlayerImage(playerID: Int? = 1078255): Bitmap? {
    Log.v("getPlayerImage", "Getting player image with playerID " + playerID.toString())
    val apiURL = "player/" + playerID.toString() + "/image"
    return getAPIImage(apiURL, APIKEY)
}
suspend fun getTeamImage(teamID: Int? = 372647): Bitmap? {
    val apiURL = "team/" + teamID.toString() + "/image"
    return getAPIImage(apiURL, APIKEY)
}

suspend fun getPreviousMatches(teamID: Int, pageID: Int = 0):APIResponse.EventsWrapper{
    return getAPIResponse("team/"+teamID.toString()+"/matches/previous/"+ pageID, APIKEY, APIResponse.EventsWrapper::class.java) as APIResponse.EventsWrapper
}

/**
 * I couldn't get coil to work with the whole APIkey, MVVM model and stuff
 * If you can, feel free to, but this slightly convoluted thing works
 */
private suspend fun getAPIImage(apiURL: String, apiKEY: String): Bitmap?{


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
    val base64String = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)
    val decodedImage: ByteArray = android.util.Base64.decode(base64String, 0)
    val bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size)

    if (bitmap == null){
       Log.d("getAPIImage", "Bitmap is null, probably because player image does not exist")
    }
    return bitmap

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
    val acceptableUserCount = 200
    val tournamentWrapper = getAPIResponse(
        "tournament/all/category/$catID",
        APIKEY,
        APIResponse.TournamentWrapper::class.java
    ) as APIResponse.TournamentWrapper
    val tournamentIDs: MutableList<Int> = mutableListOf()
    var i  = 0
    for (tournament in tournamentWrapper.uniqueTournament[0].wrapper) {
        if (tournament.userCount!! > acceptableUserCount) {
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

    Log.i("getAPIResponse", "JSON IS: " + jsonString!!.substring(0,100) + "...")


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