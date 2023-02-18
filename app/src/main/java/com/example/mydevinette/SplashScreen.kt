package com.example.mydevinette

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val thread:Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                    finish()
                } catch (e: Exception) {
                }
            }
        }
        thread.start ()

    }
}