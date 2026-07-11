package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppAd.AdMode
import com.startapp.sdk.adsbase.StartAppSDK
import com.startapp.sdk.adsbase.adlisteners.AdEventListener
import com.startapp.sdk.adsbase.adlisteners.VideoListener

object AdsManager {

    private const val TAG = "AdsManager"

    // Start.io (portal.start.io) theke paoa App ID.
    // Test kora jai Start.io er official demo App ID "205489527" diye,
    // real App ID boshanor age eta diye check kore nite paren.
    private const val APP_ID = "206256984"

    private var isInitialized = false

    private var interstitialAd: StartAppAd? = null
    private var rewardedAd: StartAppAd? = null

    private var isInterstitialReady = false
    private var isRewardedReady = false

    fun initialize(context: Context) {
        if (isInitialized) return

        StartAppSDK.initParams(context, APP_ID)
            .setReturnAdsEnabled(false) // background theke ferar somoy hothat ad dekhano bondho
            .setCallback {
                isInitialized = true
                Log.d(TAG, "Start.io SDK initialized successfully")
                loadInterstitial(context)
                loadRewarded(context)
            }
            .init()
    }

    fun isReady(): Boolean = isInitialized

    // ---------- INTERSTITIAL ----------

    fun loadInterstitial(context: Context) {
        isInterstitialReady = false
        val ad = StartAppAd(context)
        ad.loadAd(object : AdEventListener {
            override fun onReceiveAd(loadedAd: Ad) {
                interstitialAd = ad
                isInterstitialReady = true
                Log.d(TAG, "Interstitial loaded")
            }

            override fun onFailedToReceiveAd(failedAd: Ad?) {
                isInterstitialReady = false
                Log.e(TAG, "Interstitial failed to load")
            }
        })
    }

    /**
     * Interstitial dekhay. Ready na thakle show na kore shudhu
     * abar load kore rakhe, jate app crash/silent fail na hoy.
     * onClosed shob khetreई call hoy jate navigation/flow atke na thake.
     */
    fun showInterstitial(activity: Activity, onClosed: () -> Unit = {}) {
        val ad = interstitialAd
        if (!isInterstitialReady || ad == null) {
            Log.w(TAG, "Interstitial not ready yet, skipping show and reloading")
            loadInterstitial(activity)
            onClosed()
            return
        }

        val shown = ad.showAd()
        if (!shown) {
            Log.e(TAG, "Interstitial showAd() returned false")
        }
        isInterstitialReady = false
        onClosed()
        loadInterstitial(activity) // porerbar er jonno abar load kore rakhun
    }

    // ---------- REWARDED ----------

    fun loadRewarded(context: Context) {
        isRewardedReady = false
        val ad = StartAppAd(context)
        ad.loadAd(AdMode.REWARDED_VIDEO, object : AdEventListener {
            override fun onReceiveAd(loadedAd: Ad) {
                rewardedAd = ad
                isRewardedReady = true
                Log.d(TAG, "Rewarded loaded")
            }

            override fun onFailedToReceiveAd(failedAd: Ad?) {
                isRewardedReady = false
                Log.e(TAG, "Rewarded failed to load")
            }
        })
    }

    fun showRewarded(
        activity: Activity,
        onRewarded: () -> Unit,
        onFailedOrSkipped: () -> Unit = {}
    ) {
        val ad = rewardedAd
        if (!isRewardedReady || ad == null) {
            Log.w(TAG, "Rewarded not ready yet, skipping show and reloading")
            loadRewarded(activity)
            onFailedOrSkipped()
            return
        }

        var wasRewarded = false
        ad.setVideoListener {
            wasRewarded = true
            onRewarded()
        }

        val shown = ad.showAd()
        if (!shown) {
            Log.e(TAG, "Rewarded showAd() returned false")
            onFailedOrSkipped()
        }

        isRewardedReady = false
        loadRewarded(activity) // porerbar er jonno abar load kore rakhun

        // Jodi ad dekhano shuru hoyeo VideoListener theke reward na ashe
        // (user tara-tari beriye gele), tahole onFailedOrSkipped() call kora
        // caller er nijer dayittwo - simplicity er jonno ekhane extra
        // delayed-check rakha hoyni.
    }
}
