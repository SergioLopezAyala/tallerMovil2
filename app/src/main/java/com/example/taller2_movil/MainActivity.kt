package com.example.taller2_movil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.taller2_movil.ui.PhotoboothApp
import com.example.taller2_movil.ui.theme.Taller2_MovilTheme  // <-- usa tu Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Taller2_MovilTheme {    // si tu función de tema se llama distinto, pon ese nombre aquí
                PhotoboothApp()      // decide: permisos → cámara
            }
        }
    }
}
