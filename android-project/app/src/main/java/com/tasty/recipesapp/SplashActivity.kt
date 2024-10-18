package com.tasty.recipesapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tasty.recipesapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // we can access every object on the layout in ActivitySplashBinding
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_splash)

        binding.imageView.setImageResource(R.drawable.recipehub_icon)
        // You can also log to confirm the ImageView is being accessed
        Log.i("SplashActivity", "ImageView set up with image.")

        val handlerThread = HandlerThread("SplashHandlerThread", -10)
        handlerThread.start() // Create a Handler on the new HandlerThread
        val handler = Handler(handlerThread.looper)
        handler.postDelayed({
            // Navigate to MainActivity after the delay
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish() }, 2000)
    }

    override fun onStart()
    {
        super.onStart()
        // .i is an information
        Log.i("SplashActivity", "onStart: SplashActivity started.")
    }

    override fun onResume()
    {
        super.onResume()
        Log.i("SplashActivity", "onResume: SplashActivity resumed.")
    }

    override fun onPause()
    {
        super.onPause()
        Log.i("SplashActivity", "onPause: SplashActivity paused.")
    }

    override fun onStop()
    {
        super.onStop()
        Log.i("SplashActivity", "onStop: SplashActivity stopped.")
    }

    override fun onDestroy()
    {
        super.onDestroy()
        Log.i("SplashActivity", "onDestroy: SplashActivity destroyed.")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("SplashActivity", "onRestart: SplashActivity restarted.")
    }
}