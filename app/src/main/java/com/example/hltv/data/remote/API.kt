package com.example.hltv.data.remote
import okhttp3.Request

const val APIKEY = "0bb790a9cemsh84cfcecde257781p164a86jsn329610f90740"
const val ONLYCS = true //This seems unnecessary but we ball


/*
sealed class APIResponse{
    data class EventsWrapper(val event: List<Event>) : APIResponse()
    data class Lineup(val event: List<Event>) : APIResponse()

    //More to be added here
}

 */
/**
 * @return: Returns an object of type eventsWrapper, which just contains a list of "event",
 * event being the api's name for a match
 */

/**
 * Returns live matches
 */
fun getLiveMatches(): APIResponse.EventsWrapper? {

    val eventsWrapper = getAPIResponse("matches/live", APIKEY, APIResponse.EventsWrapper::class.java) as APIResponse.EventsWrapper
    if (ONLYCS){
        val csEvents: MutableList<Event> = mutableListOf()
        for (event in eventsWrapper.events){//We should also be able to use slug or flag instead of name
            if(event.tournament?.category?.name.equals("CS:GO")){
                print("Adding CSGO event " + event.tournament?.name + "\n")
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
fun getPlayersFromEvent(eventID: Int? = 10945127): APIResponse.Lineup {
    print(eventID)
    return getAPIResponse("event/" + eventID.toString() + "/lineups", APIKEY,APIResponse.Lineup::class.java) as APIResponse.Lineup
}




fun getPreviousMatches(teamID: Int, pageID: Int = 0 ):APIResponse.EventsWrapper{

    return getAPIResponse("team/"+teamID.toString()+"/matches/previous/"+ pageID, APIKEY, APIResponse.EventsWrapper::class.java) as APIResponse.EventsWrapper
}


private fun getAPIResponse(apiURL: String, apiKEY: String, classtopass:Class<*>): APIResponse {


    val request = Request.Builder()
        .url("https://allsportsapi2.p.rapidapi.com/api/esport/$apiURL")
        .get()
        .addHeader("X-RapidAPI-Key", apiKEY)
        .addHeader("X-RapidAPI-Host", "allsportsapi2.p.rapidapi.com")
        .build()

    val client = OkHttpClientSingleton.instance
    val response = client.newCall(request).execute()
    // Get the HTTP response as a string
    val jsonString = response.body()?.string()

    //Initiating as late as possible for performance reasons. Don't think it makes much of a difference
    val gson = GsonSingleton.instance
    //val regexForPlayersFromEvent = Regex("event/[0-9]+/lineups")

    response.close()

    return gson.fromJson(jsonString, classtopass) as APIResponse
}

fun main() {

    val a = getPreviousMatches(364425,0)
    print(a)




}