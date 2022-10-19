package com.example.projectara.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.example.projectara.databinding.ActivitySplashBinding
import com.example.projectara.firebase.FireStoreClass

class SplashActivity : BaseActivity() {

    private var binding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val typeFace = Typeface.createFromAsset(assets, "carbon bl.ttf")
        binding?.splash?.typeface = typeFace

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUserID = FireStoreClass().getId()

            if (currentUserID.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, 2500)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}