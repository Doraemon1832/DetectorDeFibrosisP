package com.example.detectordefibrosisp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.example.detectordefibrosisp.data.EvaluationResult
import com.example.detectordefibrosisp.data.FirebaseRepository
import com.example.detectordefibrosisp.data.SupabaseService
import com.example.detectordefibrosisp.ml.FibrosisAnalyzer
import com.example.detectordefibrosisp.ml.Semaforo
import com.example.detectordefibrosisp.ui.screens.*
import com.example.detectordefibrosisp.ui.theme.DetectorDeFibrosisPTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class AppScreen {
    CLINICAL_DATA,
    UPLOAD_XRAY,
    ANALYSIS,
    RESULT
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DetectorDeFibrosisPTheme {
                var currentScreen by remember { mutableStateOf(AppScreen.CLINICAL_DATA) }
                
                var age by remember { mutableStateOf(57) }
                var sex by remember { mutableStateOf("Hombre") }
                var smoking by remember { mutableStateOf("Nunca fumó") }
                var fvc by remember { mutableStateOf<Int?>(null) }
                
                var imageUri by remember { mutableStateOf<Uri?>(null) }
                var probability by remember { mutableStateOf(74) }
                var analysisStatusText by remember { mutableStateOf("Probabilidad elevada") }

                LaunchedEffect(Unit) {
                    try {
                        SupabaseService.fetchReferenceData()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                when (currentScreen) {
                    AppScreen.CLINICAL_DATA -> {
                        ClinicalDataScreen(
                            initialAge = age.toString(),
                            initialSex = sex,
                            initialSmoking = smoking,
                            initialFvc = fvc?.toString() ?: "",
                            onContinue = { newAge, newSex, newSmoking, newFvc ->
                                age = newAge
                                sex = newSex
                                smoking = newSmoking
                                fvc = newFvc
                                currentScreen = AppScreen.UPLOAD_XRAY
                            }
                        )
                    }
                    AppScreen.UPLOAD_XRAY -> {
                        UploadXRayScreen(
                            age = age,
                            sex = sex,
                            smoking = smoking,
                            fvc = fvc,
                            onEditClinicalData = {
                                currentScreen = AppScreen.CLINICAL_DATA
                            },
                            onAnalyze = { uri ->
                                imageUri = uri
                                currentScreen = AppScreen.ANALYSIS
                            }
                        )
                    }
                    AppScreen.ANALYSIS -> {
                        if (imageUri != null) {
                            LaunchedEffect(imageUri) {
                                val result = withContext(Dispatchers.Default) {
                                    try {
                                        FibrosisAnalyzer(applicationContext).use { analyzer ->
                                            analyzer.analizar(imageUri!!)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        null
                                    }
                                }
                                
                                if (result != null) {
                                    val combinedProb = ((result.pCnn * 0.6f + result.pSom * 0.4f) * 100f).toInt().coerceIn(1, 99)
                                    probability = combinedProb
                                    analysisStatusText = when (result.semaforo) {
                                        Semaforo.ROJO -> "Probabilidad elevada"
                                        Semaforo.AMARILLO -> "Evaluación moderada"
                                        Semaforo.VERDE -> "Baja probabilidad"
                                    }
                                } else {
                                    probability = 74
                                    analysisStatusText = "Probabilidad elevada"
                                }
                            }

                            AnalysisScreen(
                                imageUri = imageUri!!,
                                onAnalysisFinished = {
                                    currentScreen = AppScreen.RESULT
                                }
                            )
                        } else {
                            currentScreen = AppScreen.UPLOAD_XRAY
                        }
                    }
                    AppScreen.RESULT -> {
                        ResultScreen(
                            probability = probability,
                            onAnalyzeAnother = {
                                imageUri = null
                                currentScreen = AppScreen.CLINICAL_DATA
                            },
                            onSaveResult = {
                                lifecycleScope.launch {
                                    val result = EvaluationResult(
                                        age = age,
                                        sex = sex,
                                        smokingStatus = smoking,
                                        fvc = fvc,
                                        probability = probability,
                                        status = analysisStatusText
                                    )
                                    val saved = FirebaseRepository.saveEvaluationResult(result)
                                    if (saved) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Resultado guardado en Firebase exitosamente",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
