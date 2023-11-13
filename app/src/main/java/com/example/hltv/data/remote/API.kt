package com.example.hltv.data.remote
import okhttp3.Request

const val APIKEY = "0bb790a9cemsh84cfcecde257781p164a86jsn329610f90740"
const val ONLYCS = false //This seems unnecessary but we ball

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
    return getAPIResponse("event/" + eventID.toString() + "/lineups", APIKEY, APIResponse.Lineup::class.java) as APIResponse.Lineup
}




fun getPreviousMatches(teamID: Int, pageID: Int = 0 ):APIResponse.EventsWrapper{

    return getAPIResponse("team/"+teamID.toString()+"/matches/previous/"+ pageID, APIKEY, APIResponse.EventsWrapper::class.java) as APIResponse.EventsWrapper
}

/**
 * @param desiredClass The class to pass to gson. This is the same as your return class, e.g. APIResponse.Lineup::class.java
 */
private fun getAPIResponse(apiURL: String, apiKEY: String, desiredClass:Class<*>): APIResponse {

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
    response.close()

    //Initiating as late as possible for performance reasons. Don't think it makes much of a difference
    val gson = GsonSingleton.instance
    return gson.fromJson(jsonString, desiredClass) as APIResponse
}

fun main() {

    val a = getPreviousMatches(364425,0)
    val b = getLiveMatches()
    val c = getPlayersFromEvent(b?.events?.get(0)?.id)
    print(a)




}