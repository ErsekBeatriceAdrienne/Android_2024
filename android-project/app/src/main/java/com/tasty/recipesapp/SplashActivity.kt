package com.tasty.recipesapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tasty.recipesapp.databinding.ActivitySpalshBinding

class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // we can access every object on the layout in ActivitySplashBinding
        val binding = ActivitySpalshBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_splash)

        binding.button.setOnClickListener {
            Log.i(TAG, "Button pressed")

            val message = binding.textView.text.toString()
            val intent = Intent(this, MainActivity :: class.java)
            // sending the data from the splash activity
            intent.putExtra("message", message)
            // start the activity
            startActivity(intent)
            finish()
        }
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