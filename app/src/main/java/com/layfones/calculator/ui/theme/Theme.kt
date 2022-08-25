package com.layfones.calculator.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = CalcColors(
    backgroundColor = Dark,
    buttonTextColor = DarkText,
    resultColor = DarkText,
    recordColor = DarkText,
    shadowLeftTopColor = DarkShadowLeftTop,
    shadowRightBottomColor = DarkShadowRightBottom
)

private val LightColorPalette = CalcColors(
    backgroundColor = Light,
    buttonTextColor = LightText,
    resultColor = LightText,
    recordColor = LightText,
    shadowLeftTopColor = LightShadowLeftTop,
    shadowRightBottomColor = LightShadowRightBottom
)
private val LocalCalcColors = compositionLocalOf {
    LightColorPalette
}

object CalcTheme {
    val colors: CalcColors
        @Composable
        get() = LocalCalcColors.current

    enum class Theme {
        Light, Dark
    }
}

@Composable
fun CalcTheme(theme: CalcTheme.Theme = CalcTheme.Theme.Light, content: @Composable() () -> Unit) {
    val targetColors = when (theme) {
        CalcTheme.Theme.Light -> LightColorPalette
        CalcTheme.Theme.Dark -> DarkColorPalette
    }

    val backgroundColor = animateColorAsState(targetColors.backgroundColor, TweenSpec(600))
    val buttonTextColor = animateColorAsState(targetColors.buttonTextColor, TweenSpec(600))
    val resultColor = animateColorAsState(targetColors.resultColor, TweenSpec(600))
    val recordColor = animateColorAsState(targetColors.recordColor, TweenSpec(600))
    val shadowLeftTopColor = animateColorAsState(targetColors.shadowLeftTopColor, TweenSpec(600))
    val shadowRightBottomColor =
        animateColorAsState(targetColors.shadowRightBottomColor, TweenSpec(600))

    val colors = CalcColors(
        backgroundColor = backgroundColor.value,
        buttonTextColor = buttonTextColor.value,
        resultColor = resultColor.value,
        recordColor = recordColor.value,
        shadowLeftTopColor = shadowLeftTopColor.value,
        shadowRightBottomColor = shadowRightBottomColor.value
    )

    CompositionLocalProvider(LocalCalcColors provides colors) {
        MaterialTheme(shapes = Shapes, content = content)
    }
}

@Stable
class CalcColors(
    backgroundColor: Color,
    buttonTextColor: Color,
    resultColor: Color,
    recordColor: Color,
    shadowLeftTopColor: Color,
    shadowRightBottomColor: Color,
) {
    var backgroundColor: Color by mutableStateOf(backgroundColor)
        private set
    var buttonTextColor: Color by mutableStateOf(buttonTextColor)
        private set
    var resultColor: Color by mutableStateOf(resultColor)
        private set
    var recordColor: Color by mutableStateOf(recordColor)
        private set
    var shadowLeftTopColor: Color by mutableStateOf(shadowLeftTopColor)
        private set
    var shadowRightBottomColor: Color by mutableStateOf(shadowRightBottomColor)
        private set
}