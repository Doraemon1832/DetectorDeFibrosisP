package com.example.detectordefibrosisp.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class EvaluationResult(
    val age: Int,
    val sex: String,
    val smokingStatus: String,
    val fvc: Int?,
    val probability: Int,
    val status: String,
    val timestamp: Long = System.currentTimeMillis()
)

object FirebaseRepository {
    private const val TAG = "FirebaseRepository"

    suspend fun saveEvaluationResult(result: EvaluationResult): Boolean {
        return try {
            val db = FirebaseFirestore.getInstance()
            val data = hashMapOf(
                "age" to result.age,
                "sex" to result.sex,
                "smokingStatus" to result.smokingStatus,
                "fvc" to result.fvc,
                "probability" to result.probability,
                "status" to result.status,
                "timestamp" to result.timestamp
            )
            db.collection("fibrosis_evaluations")
                .add(data)
                .await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error saving evaluation result to Firebase", e)
            true
        }
    }
}
