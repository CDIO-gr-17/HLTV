package com.example.hltv.data.remote
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.ConditionVariable
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.delay
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

    //TODO: This needs to be optimized because it assumes that all other operations take 0 time
    mutexForAPI.withLock {

        delay(166)
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
 * @param desiredClass The class to pass to gson. This is the same as your return class, e.g. APIResponse.Lineup::class.java
 */
private suspend fun getAPIResponse(apiURL: String, apiKEY: String, desiredClass:Class<*>): APIResponse {

    var jsonString : String?
    var tries = 3
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
            }else {
                Log.i("getAPIResponse", "Got json: "/* + jsonString*/)
            }
        }else {
            Log.i("getAPIResponse", "jsonString is null")
        }
        tries--
    }while (jsonString?.compareTo("") == 0 && tries > 0)

    if (jsonString?.compareTo("") == 0){
        Log.e("getAPIResponse", "jsonString is repeatedly null", IOException("STRING IS NULL"))
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