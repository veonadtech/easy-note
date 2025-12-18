package com.veon.prebid.demo.ui.ads

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.prebid.mobile.AdSize
import org.prebid.mobile.api.exceptions.AdException
import org.prebid.mobile.api.rendering.BannerView
import org.prebid.mobile.api.rendering.listeners.BannerViewListener
import org.prebid.mobile.eventhandlers.GamBannerEventHandler

private const val CONFIG_ID = "veon_demo_android_320x50_banner"
private const val AD_UNIT_ID = "/23081467975/beeline_uzbekistan_android/beeline_uz_android_universal_320x50_test2"

@Composable
fun BannerView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { context ->
                // Creates view
                android.widget.FrameLayout(context).apply {

                    // listener for wrapping GAM rendering
                    val eventHandler = GamBannerEventHandler(context, AD_UNIT_ID, AdSize(320, 50))

                    // configure banner placement
//                    val adUnit = BannerView(context, CONFIG_ID, eventHandler)
                    val adUnit = BannerView(context, CONFIG_ID, AdSize(320, 50))

                    // lister for custom tracking or custom display creative
                    adUnit.setBannerListener(object : BannerViewListener {

                        override fun onAdLoaded(bannerView: BannerView?) {
                            Toast.makeText(context, "onAdLoaded", Toast.LENGTH_LONG).show()
                        }

                        override fun onAdDisplayed(bannerView: BannerView?) {
                            Toast.makeText(context, "onAdDisplayed", Toast.LENGTH_LONG).show()
                        }

                        override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {
                            Toast.makeText(context, "onAdFailed", Toast.LENGTH_LONG).show()
                        }

                        override fun onAdClicked(bannerView: BannerView?) {
                            Toast.makeText(context, "onAdClicked", Toast.LENGTH_LONG).show()
                        }

                        override fun onAdClosed(bannerView: BannerView?) {
                            Toast.makeText(context, "onAdClosed", Toast.LENGTH_LONG).show()
                        }
                    })

                    this.addView(adUnit)
                    adUnit.loadAd()
                }
            },
        )
    }
}