package com.example.hltv.data.remote
//import okhttp4.OkHttpClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import okhttp3.OkHttpClient

object GsonSingleton {
    val instance: Gson by lazy {
        Gson()
    }
}
object OkHttpClientSingleton {
    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
}

object DatabaseSingleton {
    val db : FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    /* OLD
    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore
*/
}
