package com.veon.prebid.demo.ui.ads

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.veon.prebid.demo.ui.utils.findAndroidActivity
import org.prebid.mobile.api.exceptions.AdException
import org.prebid.mobile.api.rendering.RewardedAdUnit
import org.prebid.mobile.api.rendering.listeners.RewardedAdUnitListener
import org.prebid.mobile.eventhandlers.GamRewardedEventHandler
import org.prebid.mobile.rendering.interstitial.rewarded.Reward

private const val CONFIG_ID = "prebid-demo-video-rewarded-endcard-time-close-button"
private const val AD_UNIT_ID = "/21808260008/prebid-demo-app-original-api-video-interstitial"

@Composable
fun RewardedVideoView(

    onFinish: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentSize()
    ) {

        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { context ->
                // Creates view
                android.widget.FrameLayout(context).apply {

                    val eventHandler = GamRewardedEventHandler(context.findAndroidActivity(), AD_UNIT_ID)

//                    val adUnit = RewardedAdUnit(context, CONFIG_ID, eventHandler)
                    val adUnit = RewardedAdUnit(context, CONFIG_ID)

                    adUnit.setRewardedAdUnitListener(object : RewardedAdUnitListener {
                        override fun onAdLoaded(rewardedAdUnit: RewardedAdUnit?) {
                            adUnit.show()
                        }

                        override fun onAdDisplayed(rewardedAdUnit: RewardedAdUnit?) {}
                        override fun onAdFailed(rewardedAdUnit: RewardedAdUnit?, exception: AdException?) {
                            onFinish()
                        }

                        override fun onAdClicked(rewardedAdUnit: RewardedAdUnit?) {}
                        override fun onAdClosed(rewardedAdUnit: RewardedAdUnit?) {}
                        override fun onUserEarnedReward(rewardedAdUnit: RewardedAdUnit?, reward: Reward?) {
                            Log.d("AdExample", "User earned reward: $reward")
                            onFinish()
                        }
                    })
                    adUnit.loadAd()
                }
            },
        )
    }
}