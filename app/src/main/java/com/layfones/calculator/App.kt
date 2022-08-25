package com.layfones.calculator

import android.app.Application
import androidx.annotation.Keep

class App:Application() {

    override fun onCreate() {
        super.onCreate()
    }

    @Keep
    fun mzNightModeUseOf(): Int {
        return 1
    }

}