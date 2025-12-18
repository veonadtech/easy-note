package com.veon.prebid.demo

import android.app.Application
import android.util.Log
import org.prebid.mobile.PrebidMobile
import org.prebid.mobile.api.data.InitializationStatus

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initPrebid()
    }

    private fun initPrebid() {
        PrebidMobile.setPrebidServerAccountId("org.prebid.veondemo")
        PrebidMobile.setCustomStatusEndpoint("https://prebid.veonadx.com/status")
        PrebidMobile.setTimeoutMillis(3000)
        PrebidMobile.setShareGeoLocation(true)
        PrebidMobile.setLogLevel(PrebidMobile.LogLevel.DEBUG)

        PrebidMobile.initializeSdk(
            applicationContext,
            "https://prebid.veonadx.com/openrtb2/auction",
            "https://dcdn.veonadx.com/sdk/uz.beeline.odp/config.json"
        ) { status ->
            if (status == InitializationStatus.SUCCEEDED) {
                Log.d("AAAA", "SDK initialized successfully!")
            } else {
                Log.e("AAAA", "SDK initialization error: $status\n${status.description}")
            }
        }
    }
}