package com.theprime.primechatters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adsmedia.adsmodul.AdsHelper
import com.adsmedia.adsmodul.OpenAds
import com.adsmedia.adsmodul.utils.AdsConfig
import com.theprime.primechatters.configs.AdsData

@SuppressLint("CustomSplashScreen")
class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AdsHelper.initializeAdsPrime(this, BuildConfig.APPLICATION_ID, AdsConfig.Game_App_ID)
        if (BuildConfig.DEBUG) {
            AdsHelper.debugModePrime(true)
        }
        AdsData.getIDAds();
        OpenAds.LoadOpenAds(AdsConfig.Open_App_ID)
        OpenAds.AppOpenAdManager.showAdIfAvailable(this) {
            val intent = Intent(this, MainActivity::class.java)
            object : CountDownTimer(3000, 1000) {
                override fun onFinish() {
                    startActivity(intent)
                    finish()
                }
                override fun onTick(millisUntilFinished: Long) {
                }
            }.start()
        }
    }
}