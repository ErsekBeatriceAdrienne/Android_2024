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

        val binding = ActivitySpalshBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val inputText = binding.editText.text.toString()

            if (inputText.isNotEmpty())
            {
                // an object from main activity
                val intent = Intent(this, MainActivity :: class.java)
                // sending the data from the splash activity
                intent.putExtra("input_text", inputText)
                // start the activity
                startActivity(intent)
                finish()
            }
            else Log.d(TAG, "onCreate: SplashActivity created.")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: SplashActivity started.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: SplashActivity resumed.")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: SplashActivity paused.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: SplashActivity stopped.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: SplashActivity destroyed.")
    }
}