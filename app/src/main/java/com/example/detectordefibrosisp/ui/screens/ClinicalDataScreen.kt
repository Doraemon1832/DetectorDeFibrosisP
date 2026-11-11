package com.example.detectordefibrosisp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.detectordefibrosisp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicalDataScreen(
    initialAge: String,
    initialSex: String,
    initialSmoking: String,
    initialFvc: String,
    onContinue: (age: Int, sex: String, smoking: String, fvc: Int?) -> Unit
) {
    var ageText by remember { mutableStateOf(initialAge) }
    var selectedSex by remember { mutableStateOf(initialSex) }
    var selectedSmoking by remember { mutableStateOf(initialSmoking) }
    var fvcText by remember { mutableStateOf(initialFvc) }
    var agreed by remember { mutableStateOf(false) }

    val isFormValid = ageText.toIntOrNull()?.let { it in 18..100 } == true &&
            selectedSex.isNotEmpty() &&
            selectedSmoking.isNotEmpty() &&
            agreed

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrimaryBlue,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("FibroPredict", fontWeight = FontWeight.Bold, fontSize = 18.dp.value.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue, titleContentColor = Color.White),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SecondaryBlue
            ) {
                Text(
                    text = "PASO 1 DE 2",
                    color = PrimaryBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Text(
                text = "Cuéntanos sobre ti",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Estos datos clínicos ayudan a contextualizar el análisis de tu radiografía.",
                fontSize = 14.sp,
                color = TextSecondary
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SecondaryBlue,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, tint = PrimaryBlue)
                            }
                        }
                        Column {
                            Text("Información clínica", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                            Text("Los campos con * son obligatorios", fontSize = 12.sp, color = TextSecondary)
                        }
                    }

                    HorizontalDivider(color = BorderColor)

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Edad *", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                        OutlinedTextField(
                            value = ageText,
                            onValueChange = { ageText = it },
                            placeholder = { Text("Ej. 56") },
                            suffix = { Text("años") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = BorderColor
                            )
                        )
                        Text("Debes tener entre 18 y 100 años.", fontSize = 12.sp, color = TextSecondary)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Sexo *", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            listOf("Hombre", "Mujer").forEach { sex ->
                                val isSelected = selectedSex == sex
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) PrimaryBlue else BorderColor,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .background(if (isSelected) SecondaryBlue.copy(alpha = 0.5f) else SurfaceWhite)
                                        .clickable { selectedSex = sex },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { selectedSex = sex },
                                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                                        )
                                        Text(sex, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = TextPrimary)
                                    }
                                }
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Tabaquismo *", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                        listOf("Nunca fumó", "Ex-fumador", "Fuma actualmente").forEach { smoking ->
                            val isSelected = selectedSmoking == smoking
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) PrimaryBlue else BorderColor,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(if (isSelected) SecondaryBlue.copy(alpha = 0.5f) else SurfaceWhite)
                                    .clickable { selectedSmoking = smoking },
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedSmoking = smoking },
                                        colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                                    )
                                    Text(smoking, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = TextPrimary)
                                }
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Capacidad vital forzada (FVC)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SecondaryBlue
                            ) {
                                Text(
                                    text = "OPCIONAL",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        OutlinedTextField(
                            value = fvcText,
                            onValueChange = { fvcText = it },
                            placeholder = { Text("Ej. 3200") },
                            suffix = { Text("mL") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = BorderColor
                            )
                        )
                        Text("Dato de espirometría entre 500 y 7000 mL. Si no lo tienes, déjalo vacío.", fontSize = 12.sp, color = TextSecondary)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                            .background(BackgroundLight)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Checkbox(
                            checked = agreed,
                            onCheckedChange = { agreed = it },
                            colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                        )
                        Text(
                            text = "Acepto el uso de estos datos para generar una estimación orientativa. Entiendo que no sustituye un diagnóstico médico.",
                            fontSize = 12.sp,
                            color = TextPrimary,
                            modifier = Modifier.clickable { agreed = !agreed }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val age = ageText.toIntOrNull() ?: 56
                    val fvc = fvcText.toIntOrNull()
                    onContinue(age, selectedSex, selectedSmoking, fvc)
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text("Continuar a la radiografía", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
