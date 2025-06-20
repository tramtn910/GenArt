package com.tramnt.genart

import android.app.Application
import com.tramnt.genart.di.AppContainer

class GenArtApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this)
    }
}