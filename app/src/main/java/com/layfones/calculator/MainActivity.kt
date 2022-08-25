package com.layfones.calculator

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.layfones.calculator.ui.screens.CalculatorScreen
import com.layfones.calculator.ui.theme.CalcTheme

const val TAG = "计算器"

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CalcTheme(if (viewModel.dark) CalcTheme.Theme.Dark else CalcTheme.Theme.Light) {
                CalculatorScreen()
            }
        }
    }
}