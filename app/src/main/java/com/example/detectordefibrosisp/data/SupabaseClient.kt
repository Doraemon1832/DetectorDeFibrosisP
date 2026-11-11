package com.example.detectordefibrosisp.data

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FvcReferenceRow(
    val patient: String? = null,
    val weeks: Int? = null,
    val fvc: Int? = null,
    val percent: Double? = null,
    val age: Int? = null,
    val sex: String? = null,
    val smokingstatus: String? = null
)

object SupabaseService {
    private const val SUPABASE_URL = "https://bezyeohjqczhhawxmgzj.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_NpAcEm5JJGdYwVF6s5Rpbg_cFqUsADT"

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun fetchReferenceData(): List<FvcReferenceRow> {
        return try {
            val response: HttpResponse = client.get("$SUPABASE_URL/rest/v1/fvc_referencia?select=*&limit=50") {
                header("apikey", SUPABASE_KEY)
                header("Authorization", "Bearer $SUPABASE_KEY")
            }
            if (response.status.value in 200..299) {
                Json.decodeFromString<List<FvcReferenceRow>>(response.bodyAsText())
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
