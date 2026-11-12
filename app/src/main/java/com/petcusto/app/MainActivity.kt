package com.petcusto.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.petcusto.app.ui.PetCustoApp
import com.petcusto.app.ui.theme.PetCustoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetCustoTheme {
                PetCustoApp()
            }
        }
    }
}
