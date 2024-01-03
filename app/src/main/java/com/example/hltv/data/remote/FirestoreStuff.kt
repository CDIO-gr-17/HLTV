package com.example.hltv.data.remote

import android.util.Log
import kotlinx.coroutines.tasks.await

data class PredictionsWrapper(
    val predictions: Map<Int, Prediction>
)
data class Prediction(
    var homeTeamVoteCount: Int = 0,
    var awayTeamVoteCount: Int = 0,
    var homeTeamVotePercentage: Int = 50,
    var awayTeamVotePercentage: Int = 50
)

suspend fun sendPredictionToFirestore(prediction: Prediction, matchID: Int) {
    DatabaseSingleton.db.collection("predictions").document(matchID.toString()).set(prediction)
        .addOnSuccessListener { Log.d("Database","DocumentSnapshot successfully written!") }
        .addOnFailureListener { e -> Log.w("Database","Error writing document: $e") }
}

suspend fun getPredictionFromFirestore(matchID: Int): Prediction? {
    val docRef = DatabaseSingleton.db.collection("predictions").document(matchID.toString())
    var prediction: Prediction? = null
    try {
        prediction = docRef.get().await().toObject(Prediction::class.java)
        Log.w("Database","Got prediction from firestore: $prediction")
    }catch (e: Exception) {
        Log.w("Database","Error getting documents: $e")
        prediction = Prediction(0, 0)
    }
    return prediction




    /*
    try {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    prediction = document.toObject(Prediction::class.java)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Database","Error getting documents: $exception")
            }
    } catch (e: Exception) {
        Log.w("Database","Error getting documents: $e")
        prediction = Prediction(0, 0)
    }
    return prediction*/
}
