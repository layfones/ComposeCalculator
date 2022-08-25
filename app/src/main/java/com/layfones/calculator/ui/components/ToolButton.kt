package com.layfones.calculator.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.layfones.calculator.ui.theme.CalcTheme
import com.layfones.calculator.util.drawColoredShadow

@Composable
fun ToolButton(@DrawableRes icon: Int, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.Transparent)
            .clickable(interactionSource, indication = null) {
                onClick()
            }
            .drawColoredShadow(
                color = CalcTheme.colors.shadowLeftTopColor,
                0.9f,
                borderRadius =  12.dp,
                shadowRadius = 6.dp,
                offsetX = if (isPressed) 0.dp else (-6).dp,
                offsetY = if (isPressed) 0.dp else (-6).dp
            )
            .drawColoredShadow(
                color = CalcTheme.colors.shadowRightBottomColor,
                0.1f,
                borderRadius = 12.dp,
                shadowRadius = 6.dp,
                offsetX = if (isPressed) 0.dp else (6).dp,
                offsetY = if (isPressed) 0.dp else (6).dp
            )
            .background(
                CalcTheme.colors.backgroundColor, shape = RoundedCornerShape(12.dp)
            ), contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = icon), contentDescription = "dark mode")
    }
}