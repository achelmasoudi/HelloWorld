package com.achelm.helloworld.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.achelm.helloworld.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_SPEED: Long = 2400
        private const val PREFS_NAME = "MyPrefs"
        private const val LOGIN_COMPLETED_KEY = "loginCompleted"
    }

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Set navigation bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.parseColor("#FAFAFA")
        }

        mAuth = FirebaseAuth.getInstance()

        Thread {
            Thread.sleep(SPLASH_SPEED)
            runOnUiThread {
                checkLoginStatus()
            }
        }.start()

    }

    private fun checkLoginStatus() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val loginCompleted = prefs.getBoolean(LOGIN_COMPLETED_KEY, false)

//        if (!loginCompleted) {
//            // User is not authenticated, go to LoginActivity
//            startActivity(Intent(this@SplashScreenActivity, EntryActivity::class.java))
//            finish()
//        }
//        else {
//            // User is logged in, go to MainActivity
//            var intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }

        if (!loginCompleted) {
            // User is not authenticated, go to LoginActivity
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            finish()
        }
        else {
            // User is logged in, go to MainActivity
            var intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}