package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.AppScreen
import com.example.ui.DarkThemeConfig
import com.example.ui.JournalViewModel
import com.example.ui.screens.JournalListScreen
import com.example.ui.screens.NotebookCanvasScreen
import com.example.ui.theme.RuangJurnalTheme

class MainActivity : ComponentActivity() {
    private val viewModel: JournalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkThemeConfig by viewModel.darkThemeConfig.collectAsState()
            val isDark = when (darkThemeConfig) {
                DarkThemeConfig.SYSTEM -> isSystemInDarkTheme()
                DarkThemeConfig.DARK -> true
                DarkThemeConfig.LIGHT -> false
            }

            val currentScreen by viewModel.currentScreen.collectAsState()

            RuangJurnalTheme(darkTheme = isDark) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    when (currentScreen) {
                        is AppScreen.JournalList -> {
                            JournalListScreen(viewModel = viewModel)
                        }
                        is AppScreen.NotebookCanvas -> {
                            NotebookCanvasScreen(viewModel = viewModel, isDarkTheme = isDark)
                        }
                    }
                }
            }
        }
    }
}
