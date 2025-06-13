package com.example.rechnerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var resultText: TextView
    private lateinit var calculateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI-Elemente referenzieren
        inputField = findViewById(R.id.inputField)
        typeSpinner = findViewById(R.id.typeSpinner)
        resultText = findViewById(R.id.resultText)
        calculateButton = findViewById(R.id.calculateButton)

        // Spinner-Optionen setzen
        val options = listOf("Fläche in Fußballfelder", "Alter in Minuten", "Geld in Zeit")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        typeSpinner.adapter = adapter

        // Button-Click-Listener
        calculateButton.setOnClickListener {
            val input = inputField.text.toString()
            val selection = typeSpinner.selectedItem.toString()
            val result = when (selection) {
                "Fläche in Fußballfelder" -> convertAreaToFields(input)
                "Alter in Minuten" -> convertAgeToMinutes(input)
                "Geld in Zeit" -> convertMoneyToTime(input)
                else -> "Ungültige Auswahl"
            }
            resultText.text = result
        }
    }

    // Umrechnung Fläche → Fußballfelder
    private fun convertAreaToFields(input: String): String {
        val squareMeters = input.toDoubleOrNull() ?: return "Bitte gib eine gültige Zahl ein."
        val fieldSize = 7140.0 // 105m x 68m
        val numFields = squareMeters / fieldSize
        return "Das entspricht %.2f Fußballfeldern.".format(numFields)
    }

    // Umrechnung Alter → Minuten
    private fun convertAgeToMinutes(input: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val birthDate = LocalDate.parse(input, formatter)
            val today = LocalDate.now()
            val ageInMinutes = ChronoUnit.MINUTES.between(birthDate.atStartOfDay(), today.atStartOfDay())
            "Du bist etwa %,d Minuten alt.".format(ageInMinutes)
        } catch (e: Exception) {
            "Bitte gib das Geburtsdatum im Format TT.MM.JJJJ ein."
        }
    }

    // Umrechnung Geld → Zeit
    private fun convertMoneyToTime(input: String): String {
        val amount = input.toDoubleOrNull() ?: return "Bitte gib einen gültigen Geldbetrag ein."
        val seconds = amount
        val days = seconds / (60 * 60 * 24)
        val years = seconds / (60 * 60 * 24 * 365.25)
        return "Das entspricht ca. %.2f Tagen oder %.2f Jahren.".format(days, years)
    }
}
