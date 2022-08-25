package com.layfones.calculator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.layfones.calculator.MainViewModel
import com.layfones.calculator.R
import com.layfones.calculator.ui.components.Backspace
import com.layfones.calculator.ui.components.CalcButton
import com.layfones.calculator.ui.components.ToolButton
import com.layfones.calculator.ui.theme.CalcTheme
import com.layfones.calculator.util.stateList2String

@Preview(showSystemUi = true)
@Composable
fun CalculatorScreen() {
    val viewModel: MainViewModel = viewModel()
    Box(
        Modifier
            .fillMaxSize()
            .background(color = CalcTheme.colors.backgroundColor)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CalcTheme.colors.backgroundColor)

        ) {
            Toolbar(viewModel)
            Result(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                viewModel
            )
            ButtonGroup(
                Modifier
                    .fillMaxWidth(),
                viewModel
            )
        }
    }
}

@Composable
fun Toolbar(viewModel: MainViewModel) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ToolButton(if (viewModel.dark) R.drawable.ic_light_mode else R.drawable.ic_dark_mode) {
            viewModel.changeTheme()
        }
        ToolButton(R.drawable.ic_delete) {
            viewModel.clearRecord()
        }
    }
}

@Composable
fun Result(modifier: Modifier, viewModel: MainViewModel) {
    val listState = rememberLazyListState()
    LaunchedEffect(viewModel.flag) {
        listState.animateScrollToItem(index = viewModel.records.size)
    }
    LazyColumn(
        state = listState, modifier = modifier, verticalArrangement = Arrangement.Bottom
    ) {
        items(
            items = viewModel.records,
            key = { record ->
                record.id
            }
        ) { record ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = record.expression,
                    fontSize = 24.sp,
                    color = CalcTheme.colors.resultColor
                )
                Text(
                    text = record.result,
                    fontSize = 30.sp,
                    color = CalcTheme.colors.resultColor
                )
            }
        }
        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .background(CalcTheme.colors.backgroundColor),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = viewModel.currentExpression.stateList2String(),
                    fontSize = 24.sp,
                    color = CalcTheme.colors.resultColor
                )
                Text(
                    text = if (viewModel.hasError) "Error!" else viewModel.currentResult,
                    fontSize = 30.sp,
                    color = CalcTheme.colors.resultColor
                )
            }
        }

    }
}

val buttons = arrayOf(
    arrayOf("AC", "7", "4", "1", "%"),
    arrayOf("÷", "8", "5", "2", "0"),
    arrayOf("×", "9", "6", "3", "."),
    arrayOf("X", "-", "+", "="),
)

@Composable
fun ButtonGroup(modifier: Modifier, viewModel: MainViewModel) {
    Row(
        modifier
            .padding(vertical = 20.dp, horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        buttons.forEach {
            Column(
                Modifier.width(60.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                it.forEach { it ->
                    if (it == "X") {
                        Backspace() {
                            viewModel.notifyDown()
                            if (viewModel.hasError) {
                                viewModel.clear()
                            } else {
                                if (viewModel.hasResult) {
                                    viewModel.addResult()
                                    viewModel.clear()
                                } else {
                                    if (viewModel.currentExpression.size > 0) {
                                        viewModel.currentExpression.removeLast()
                                    }
                                }
                            }
                        }
                    } else {
                        CalcButton(
                            Modifier
                                .width(60.dp)
                                .height(if (it == "=") 132.dp else 60.dp),
                            symbol = it,
                        ) { symbol ->
                            viewModel.notifyDown()
                            when (symbol) {
                                "=" -> {
                                    if (viewModel.currentExpression.size > 0) {
                                        val last = viewModel.currentExpression.last()
                                        val n = arrayOf("+", "-", "×", "÷", ".")
                                        if (!n.contains(last) && !viewModel.hasError) {
                                            viewModel.calculate()
                                        }
                                    }
                                }
                                "AC" -> {
                                    if (!viewModel.hasError && viewModel.hasResult) {
                                        viewModel.addResult()
                                    }
                                    viewModel.clear()
                                }
                                "%" -> {
                                    if (viewModel.currentExpression.size > 0) {
                                        val last = viewModel.currentExpression.last()
                                        val n = arrayOf("+", "-", "×", "÷", ".")
                                        if (!n.contains(last) && !viewModel.hasError) {
                                            if (viewModel.hasResult) {
                                                viewModel.addResult()
                                                viewModel.clear()
                                                val lastResult =
                                                    viewModel.lastResult().replaceFirst("=", "")
                                                viewModel.currentExpression.add(lastResult)
                                            }
                                            viewModel.currentExpression.add("×0.01")
                                        }
                                    }
                                }
                                in "+", "-", "×", "÷" -> {
                                    if (viewModel.currentExpression.size > 0) {
                                        val last = viewModel.currentExpression.last()
                                        val n = arrayOf("+", "-", "×", "÷")
                                        if (!n.contains(last) && !viewModel.hasError) {
                                            if (viewModel.hasResult) {
                                                viewModel.addResult()
                                                viewModel.clear()
                                                val lastResult =
                                                    viewModel.lastResult().replaceFirst("=", "")
                                                viewModel.currentExpression.add(lastResult)
                                            }
                                            viewModel.currentExpression.add(symbol)
                                        }
                                    }
                                }
                                else -> {
                                    if (viewModel.hasError) {
                                        viewModel.clear()
                                        viewModel.currentExpression.add(symbol)
                                    } else {
                                        if (viewModel.hasResult) {
                                            viewModel.addResult()
                                            viewModel.clear()
                                        }
                                        if (viewModel.currentExpression.size < 36) {
                                            viewModel.currentExpression.add(symbol)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}