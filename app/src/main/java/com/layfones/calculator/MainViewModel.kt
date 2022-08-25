package com.layfones.calculator

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.layfones.calculator.calc.MixedOperation
import com.layfones.calculator.model.Record
import com.layfones.calculator.util.stateList2String
import java.math.RoundingMode
import java.text.DecimalFormat

class MainViewModel : ViewModel() {

    var currentExpression = mutableStateListOf<String>()
    var currentResult by mutableStateOf("0")
    var hasResult by mutableStateOf(false)
    var hasError by mutableStateOf(false)
    var records = mutableStateListOf<Record>()

    var flag by mutableStateOf(false)

    var dark by mutableStateOf(false)

    fun changeTheme() {
        dark = !dark
    }

    fun clearRecord() {
        records.clear()
    }

    fun calculate() {
        try {
            val finalExp = currentExpression.stateList2String()
                .replace("×", "*").replace("÷", "/")
            val opt = MixedOperation(10)
            val result = opt.getMixedOperationRes(finalExp)
            val format = DecimalFormat().apply {
                maximumFractionDigits = 12
                roundingMode = RoundingMode.HALF_EVEN
                groupingSize = 3
            }
            currentResult = "="+format.format(result).toString()
            hasResult = true
            hasError = false
        } catch (e: Exception) {
            Log.d(TAG, "CalcPanel: 出错了$e")
            hasResult = false
            hasError = true
        }
    }

    fun lastResult(): String {
        return records.last().result
    }

    fun addResult() {
        var id = 0L
        if (records.size > 0) {
            id = records.last().id + 1
        }
        records.add(
            Record(
                id,
                currentExpression.stateList2String(),
                currentResult
            )
        )
    }

    fun clear() {
        currentExpression.clear()
        currentResult = "0"
        hasError = false
        hasResult = false
    }

    fun notifyDown() {
        flag = !flag
    }

}