package com.example.hltv.data.remote

import android.util.Log

data class PredictionsWrapper(
    val predictions: Map<Int, Prediction>
)
data class Prediction(
    val homeTeamVoteCount: Int,
    val awayTeamVoteCount: Int
)

fun sendPredictionToFirestore(prediction: Prediction, matchID: Int) {
    DatabaseSingleton.db.collection("predictions").document(matchID.toString()).set(prediction)
        .addOnSuccessListener { Log.d("Database","DocumentSnapshot successfully written!") }
        .addOnFailureListener { e -> Log.w("Database","Error writing document: $e") }
}
fun getPredictionFromFirestore(matchID: Int): Prediction? {
    val docRef = DatabaseSingleton.db.collection("predictions").document(matchID.toString())
    var prediction: Prediction? = null
    docRef.get()
        .addOnSuccessListener { document ->
            if (document != null) {
                prediction = document.toObject(Prediction::class.java)
            }
        }
        .addOnFailureListener { exception ->
            Log.w("Database","Error getting documents: $exception")
        }
    return prediction
}
