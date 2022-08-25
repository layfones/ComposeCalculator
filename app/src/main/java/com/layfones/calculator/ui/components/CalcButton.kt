package com.layfones.calculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.layfones.calculator.ui.theme.Blue
import com.layfones.calculator.ui.theme.CalcTheme
import com.layfones.calculator.util.drawColoredShadow


@Composable
fun CalcButton(
    modifier: Modifier = Modifier,
    symbol: String,
    onClick: (symbol: String) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        Modifier
            .clickable(
                interactionSource, indication = null
            ) { onClick(symbol) }
            .then(modifier)
            .background(Color.Transparent)
            .drawColoredShadow(
                CalcTheme.colors.shadowLeftTopColor,
                0.9f,
                borderRadius = 14.dp,
                shadowRadius = 7.dp,
                offsetX = if (!isPressed) (-6).dp else 0.dp,
                offsetY = if (!isPressed) (-6).dp else 0.dp
            )
            .drawColoredShadow(
                CalcTheme.colors.shadowRightBottomColor,
                0.2f,
                borderRadius = 14.dp,
                shadowRadius = 7.dp,
                offsetX = if (!isPressed) (6).dp else 0.dp,
                offsetY = if (!isPressed) (6).dp else 0.dp
            )
            .background(
                if (symbol == "=")
                    Blue else CalcTheme.colors.backgroundColor, shape = RoundedCornerShape(15.dp)
            ), contentAlignment = Alignment.Center
    ) {
        val opt = arrayOf("+", "-", "×", "÷")
        if (opt.contains(symbol)) {
            Text(text = symbol, fontSize = 36.sp, color = Blue)
        } else if (symbol == "=") {
            Text(text = symbol, fontSize = 36.sp, color = Color.White)
        }
        else {
            Text(text = symbol, fontSize = 36.sp, color = CalcTheme.colors.buttonTextColor)
        }

    }
}