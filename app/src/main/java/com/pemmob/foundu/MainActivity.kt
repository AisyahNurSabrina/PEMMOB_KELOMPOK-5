package com.pemmob.foundu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pemmob.foundu.ui.nav.FoundUApp
import com.pemmob.foundu.ui.theme.FoundUTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoundUTheme {
                FoundUApp()
            }
        }
    }
}