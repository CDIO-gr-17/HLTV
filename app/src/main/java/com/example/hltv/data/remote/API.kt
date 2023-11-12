package com.example.hltv.data.remote
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.annotations.SerializedName
val APIKEY = "478aa6c7a2msh89c2c0c24f19184p1edb29jsn1d19bce3a650"
data class Student (
    var name: String? = null,
    var address: String? = null) {
}
fun getLiveMatches(): EventsWrapper? {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://allsportsapi2.p.rapidapi.com/api/esport/matches/live")
        .get()
        .addHeader("X-RapidAPI-Key", APIKEY)
        .addHeader("X-RapidAPI-Host", "allsportsapi2.p.rapidapi.com")
        .build()

    val response = client.newCall(request).execute()

    // Get the HTTP response as a string
    val jsonString = response.body()?.string()

    val gson = Gson()
    val eventsWrapper = gson.fromJson(jsonString, EventsWrapper::class.java)

    return eventsWrapper
}

/*
object RetrofitClient {
    private const val BASE_URL = "https://allsportsapi2.p.rapidapi.com/api/esport/matches/live"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}

object AllApi {

    private external fun baseUrlFromJNI(boolean: Boolean): String

    const val BASE_URL = "https://api.example.com/"

    private const val V1 = "v1/"

    const val DATA_LIST = V1 + "my_data.php"
}
interface ApiService {
    @GET("posts/{id}")
    fun getPostById(@Path("id") postId: Int): Call<Events>
}



fun main(args : Array<String>) {
    println("Hello, World!")
}

 */