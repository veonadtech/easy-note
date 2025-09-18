package com.veon.prebid.demo.ui.ads

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.veon.prebid.demo.ui.utils.findAndroidActivity
import org.prebid.mobile.api.exceptions.AdException
import org.prebid.mobile.api.rendering.InterstitialAdUnit
import org.prebid.mobile.api.rendering.listeners.InterstitialAdUnitListener
import org.prebid.mobile.eventhandlers.GamInterstitialEventHandler

private const val CONFIG_ID = "beeline_uz_android_universal_320x50"
private const val AD_UNIT_ID = "/23081467975/beeline_uzbekistan_android/beeline_uz_android_universal_320x50"

@Composable
fun InterstitialView() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentSize()
    ) {

        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { context ->
                // Creates view
                android.widget.FrameLayout(context).apply {

                    // listener for wrapping GAM rendering
                    val eventHandler = GamInterstitialEventHandler(context.findAndroidActivity(), AD_UNIT_ID)

                    // configure banner placement
                    val adUnit = InterstitialAdUnit(context, CONFIG_ID, eventHandler)

                    // lister for custom tracking or custom display creative
                    adUnit.setInterstitialAdUnitListener(object : InterstitialAdUnitListener {
                        override fun onAdLoaded(interstitialAdUnit: InterstitialAdUnit?) {
                            adUnit.show()
                            Toast.makeText(context, "loaded", Toast.LENGTH_SHORT).show()
                        }

                        override fun onAdDisplayed(interstitialAdUnit: InterstitialAdUnit?) {
                            Toast.makeText(context, "displayed", Toast.LENGTH_SHORT).show()
                        }

                        override fun onAdFailed(interstitialAdUnit: InterstitialAdUnit?, exception: AdException?) {
                            Toast.makeText(context, "failed: $exception", Toast.LENGTH_SHORT).show()
                            Log.d("AAAA", "onAdFailed: $exception")
                        }

                        override fun onAdClicked(interstitialAdUnit: InterstitialAdUnit?) {
                            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
                        }

                        override fun onAdClosed(interstitialAdUnit: InterstitialAdUnit?) {
                            Toast.makeText(context, "closed", Toast.LENGTH_SHORT).show()
                        }
                    })
                    adUnit.loadAd()
                }
            },
        )
    }
}