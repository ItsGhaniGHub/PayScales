package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    PayScaleCalculatorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PayScaleCalculatorScreen(modifier: Modifier = Modifier) {
    // Selection state
    var selectedGrade by remember { mutableStateOf("18") } // Default grade 18
    var selectedYear by remember { mutableStateOf("2022") } // Default revision year 2022
    var selectedIncrement by remember { mutableStateOf(0) }
    var selectedHraType by remember { mutableStateOf("30%") } // 30% / 45% / Special / Ordinary
    var maritalStatus by remember { mutableStateOf("Married") } // Single / Married

    val scrollState = rememberScrollState()

    // Parsing inputs
    val gradeInt = selectedGrade.toIntOrNull() ?: 18
    val yearInt = selectedYear.toIntOrNull() ?: 2022

    // Lookup scale configurations
    val bpsScaleMap = BpsData.payScales[gradeInt] ?: emptyMap()
    val bpsScaleInfo = bpsScaleMap[yearInt] ?: BpsScaleInfo(0, 0, 14)
    val maxIncrements = bpsScaleInfo.maxIncrements

    // Keep selected increment within valid bounds of the selected BPS Grade
    LaunchedEffect(maxIncrements) {
        if (selectedIncrement > maxIncrements) {
            selectedIncrement = maxIncrements
        }
    }

    // Calculations
    val basicPay = bpsScaleInfo.minPay + (selectedIncrement * bpsScaleInfo.incrementPay)
    val rules = BpsData.allowanceRules[gradeInt] ?: AllowanceRules(0, 0, 0, 0, 0, 0)

    // HRA Type Logic
    val rawHra = when (selectedHraType) {
        "30%" -> (basicPay * 0.30).toInt()
        "45%" -> (basicPay * 0.45).toInt()
        "Special" -> rules.hraSpecial
        "Ordinary" -> rules.hraOrdinary
        else -> rules.hraOrdinary
    }
    // Marital status effect: Single receives 50% HRA, Married receives 100%
    val hraValue = if (maritalStatus == "Single") (rawHra * 0.5).toInt() else rawHra

    // Conveyance logic (BPS 20+ gets 0 as they use Car Monetization)
    val conveyanceValue = if (gradeInt >= 20) 0 else rules.conveyance

    // Medical Allowance: Flat 1500 for BPS 1-15, and 15% of basic pay for BPS 16-22
    val medicalValue = if (gradeInt >= 16) (basicPay * 0.15).toInt() else 1500

    // BPS 19 and above get Entertainment Allowance
    val entertainmentValue = if (gradeInt >= 19) rules.entertainment else 0

    // BPS 20 and above get S.Post Allowance & Car Monetization
    val sPostValue = if (gradeInt >= 20) rules.sPost else 0
    val carMonetizationValue = if (gradeInt >= 20) rules.carMonetization else 0

    // GP Fund Subscription (Deduction)
    val gpFundValue = rules.gpFund

    // Totals
    val grossPay = basicPay + hraValue + conveyanceValue + medicalValue + entertainmentValue + sPostValue + carMonetizationValue
    val netPay = grossPay - gpFundValue

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showExportSuccessDialog by remember { mutableStateOf(false) }

    val createPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val success = withContext(Dispatchers.IO) {
                    PdfExporter.exportToPdf(
                        context = context,
                        uri = uri,
                        grade = selectedGrade,
                        year = selectedYear,
                        increment = selectedIncrement,
                        maxIncrements = maxIncrements,
                        hraType = selectedHraType,
                        maritalStatus = maritalStatus,
                        basicPay = basicPay,
                        hraValue = hraValue,
                        conveyanceValue = conveyanceValue,
                        medicalValue = medicalValue,
                        entertainmentValue = entertainmentValue,
                        sPostValue = sPostValue,
                        carMonetizationValue = carMonetizationValue,
                        gpFundValue = gpFundValue,
                        grossPay = grossPay,
                        netPay = netPay
                    )
                }
                if (success) {
                    showExportSuccessDialog = true
                    Toast.makeText(context, "PDF saved successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to save PDF.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    if (showExportSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showExportSuccessDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    "PDF Saved Successfully",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    "The calculation breakdown of BPS-$selectedGrade for year $selectedYear has been saved as a beautifully formatted PDF document on your device.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { showExportSuccessDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text("Dismiss", fontWeight = FontWeight.Bold)
                    }
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Simulated Status Bar to match HTML layout exactly
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "9:41",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), RoundedCornerShape(50.dp))
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), RoundedCornerShape(50.dp))
                )
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 9.dp)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
                )
            }
        }

        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pay Calculator",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.testTag("app_title")
                )
            }
            // Reset Button in Top Bar
            TextButton(
                onClick = {
                    selectedGrade = "18"
                    selectedYear = "2022"
                    selectedIncrement = 0
                    selectedHraType = "30%"
                    maritalStatus = "Married"
                },
                modifier = Modifier.testTag("reset_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset inputs",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Reset",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Inputs Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Scale & Increment Parameters",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        // Inner secondary reset button
                        TextButton(
                            onClick = {
                                selectedGrade = "18"
                                selectedYear = "2022"
                                selectedIncrement = 0
                                selectedHraType = "30%"
                                maritalStatus = "Married"
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear all inputs",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Reset Defaults",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Side-by-side Dropdowns for Grade and Year
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SimpleDropdownField(
                            label = "BPS Grade",
                            selectedValue = "BPS-$selectedGrade",
                            options = BpsData.grades.map { "BPS-$it" },
                            onValueSelected = { selectedGrade = it.replace("BPS-", "") },
                            modifier = Modifier.weight(1f)
                        )

                        SimpleDropdownField(
                            label = "Pay Year",
                            selectedValue = selectedYear,
                            options = BpsData.years.map { it.toString() },
                            onValueSelected = { selectedYear = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Increment Stage Slider
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Increment Stage",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text("Min", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            Slider(
                                value = selectedIncrement.toFloat(),
                                onValueChange = { selectedIncrement = it.toInt() },
                                valueRange = 0f..maxIncrements.toFloat(),
                                steps = if (maxIncrements > 1) maxIncrements - 1 else 0,
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp)
                            )
                            Text("Max", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = String.format("%02d", selectedIncrement),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    // HRA & Marital Status segmented controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // HRA Category Selector
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "HRA CATEGORY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(50.dp))
                                    .padding(3.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val options = listOf("30%", "45%", "Special", "Ordinary")
                                options.forEach { type ->
                                    val isSelected = (selectedHraType == type)
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                RoundedCornerShape(50.dp)
                                            )
                                            .clickable { selectedHraType = type }
                                            .padding(vertical = 8.dp)
                                            .testTag("hra_radio_$type")
                                    ) {
                                        Text(
                                            text = if (type == "Special") "Spec" else if (type == "Ordinary") "Ord" else type,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        // Status Selector
                        Column(modifier = Modifier.width(120.dp)) {
                            Text(
                                text = "STATUS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(50.dp))
                                    .padding(3.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val statuses = listOf("Married" to "M", "Single" to "S")
                                statuses.forEach { (status, label) ->
                                    val isSelected = (maritalStatus == status)
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                RoundedCornerShape(50.dp)
                                            )
                                            .clickable { maritalStatus = status }
                                            .padding(vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Main Net Pay Result Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ESTIMATED NET MONTHLY PAY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = String.format("Rs. %,d", netPay),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.testTag("net_pay_text")
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "GROSS PAY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format("Rs. %,d", grossPay),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(Color.White.copy(alpha = 0.2f))
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "DEDUCTIONS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format("Rs. %,d", gpFundValue),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Salary Breakdown Card
            val items = remember(basicPay, hraValue, conveyanceValue, medicalValue, entertainmentValue, sPostValue, carMonetizationValue, gpFundValue, selectedHraType) {
                buildList {
                    add("Basic Pay" to (basicPay to false))
                    add("House Rent ($selectedHraType)" to (hraValue to false))
                    if (conveyanceValue > 0) {
                        add("Conveyance Allowance" to (conveyanceValue to false))
                    }
                    add("Medical Allowance" to (medicalValue to false))
                    if (entertainmentValue > 0) {
                        add("Entertainment Allowance" to (entertainmentValue to false))
                    }
                    if (sPostValue > 0) {
                        add("Senior Post Allowance" to (sPostValue to false))
                    }
                    if (carMonetizationValue > 0) {
                        add("Car Monetization" to (carMonetizationValue to false))
                    }
                    add("GP Fund (Deduction)" to (gpFundValue to true))
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "SALARY BREAKDOWN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    items.forEachIndexed { index, (label, valuePair) ->
                        val (value, isDeduction) = valuePair
                        BreakdownRow(label = label, value = value, isDeduction = isDeduction)
                        if (index < items.size - 1) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                        }
                    }
                }
            }

            // Export to PDF Button Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable {
                        val fileName = "BPS-${selectedGrade}_Pay_Breakdown_${selectedYear}.pdf"
                        createPdfLauncher.launch(fileName)
                    }
                    .testTag("export_pdf_button"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Export to PDF",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Export Breakdown as PDF",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand dropdown",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        )
        // Invisible overlay click target
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.45f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, fontSize = 14.sp) },
                    onClick = {
                        onValueSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun BreakdownRow(label: String, value: Int, isDeduction: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontWeight = FontWeight.Normal
        )
        Text(
            text = if (isDeduction) "- Rs. $value" else "Rs. $value",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDeduction) Color(0xFFBA1A1A) else MaterialTheme.colorScheme.primary
        )
    }
}
