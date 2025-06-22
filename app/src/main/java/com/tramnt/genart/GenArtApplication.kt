package com.tramnt.genart

import android.app.Application
import com.apero.aigenerate.AiServiceConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GenArtApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        AiServiceConfig.setUpData(
            projectName = "genArt",
            applicationId = "com.tramnt.genart",
            apiKey = "sk-ePKj7HupzKwrm0BBDpKgbcptFg6zhJL7Fx0ZpfOMzhTa0w2efS",
        )
        AiServiceConfig.setTimeStamp(System.currentTimeMillis())
    }
}