package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConverterScreen()
                }
            }
        }
    }
}

val exchangeRates = mapOf(
    "USD" to 16789.0,
    "EUR" to 19071.0,
    "JPY" to 117.1,
    "GBP" to 22114.70,
    "AUD" to 10640.0,
    "CAD" to 12080.0,
    "SGD" to 12750.0,
    "MYR" to 3812.0,
    "THB" to 500.3,
    "CNY" to 2294.0,
)

val targetCurrencies = exchangeRates.keys.toList()

@Composable
fun CurrencyConverterScreen() {
    var idrAmountInput by remember {
        mutableStateOf("")
    }
    var selectedCurrency by remember {
        mutableStateOf(targetCurrencies.first())
    }
    var conversionResult by remember {
        mutableStateOf<String?>(null)
    }
    var isDropdownExpanded by remember {
        mutableStateOf(false)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Konverter Mata Uang (IDR)", fontSize = 20.sp, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = idrAmountInput, onValueChange = {
            newValue ->
            if(newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                idrAmountInput = newValue
            }
            conversionResult = null
        },
            label = { Text("Jumlah IDR")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Konversi ke:", modifier = Modifier.align(Alignment.Start))
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCurrency,
                onValueChange = {}, // Tidak bisa diubah langsuns
                label = { Text("Mata Uang Tujuan") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { isDropdownExpanded = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Pilih Mata Uang")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                targetCurrencies.forEach { currency ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCurrency = currency
                            isDropdownExpanded = false
                            conversionResult = null
                        },
                        text = { Text(currency) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val idrAmount = idrAmountInput.toDoubleOrNull()
                if(idrAmountInput.isBlank()) {
                    conversionResult = "Masukkan Jumlah IDR."
                } else if (idrAmount == null || idrAmount <= 0) {
                    conversionResult = "Jumlah IDR tidak valid,"
                } else {
                    val rate = exchangeRates[selectedCurrency]
                    if(rate != null) {
                        val convertedAmount = idrAmount / rate
                        val formattedResult = String.format(Locale.US, "%.2f", convertedAmount)
                        conversionResult = "$formattedResult $selectedCurrency"
                    } else {
                        conversionResult = "Kurs tidak ditemukan"
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Konversi")
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (conversionResult != null) { // Hanya tampilkan jika ada hasil/pesan
            Text(
                text = "Hasil: $conversionResult",
                fontSize = 20.sp,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        InfoDisplay()
    }
}


@Composable
fun InfoDisplay() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nama: Moch. Avin",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = "NRP: 5025221061",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}