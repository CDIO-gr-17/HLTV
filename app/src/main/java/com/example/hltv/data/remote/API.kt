package com.example.hltv.data.remote
import okhttp3.Request
const val APIKEY = "478aa6c7a2msh89c2c0c24f19184p1edb29jsn1d19bce3a650"
const val ONLYCS = false

/**
 * @return: Returns an object of type eventsWrapper, which just contains a list of "event",
 * event being the api's name for a match
 */
fun getLiveMatches(): EventsWrapper? {


    val request = Request.Builder()
        .url("https://allsportsapi2.p.rapidapi.com/api/esport/matches/live")
        .get()
        .addHeader("X-RapidAPI-Key", APIKEY)
        .addHeader("X-RapidAPI-Host", "allsportsapi2.p.rapidapi.com")
        .build()

    val client = OkHttpClientSingleton.instance
    val response = client.newCall(request).execute()
    // Get the HTTP response as a string
    val jsonString = response.body()?.string()

    //Initiating as late as possible for performance reasons. Don't think it makes much of a difference
    val gson = GsonSingleton.instance
    val eventsWrapper = gson.fromJson(jsonString, EventsWrapper::class.java)

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

fun main() {
    print(getLiveMatches())
}