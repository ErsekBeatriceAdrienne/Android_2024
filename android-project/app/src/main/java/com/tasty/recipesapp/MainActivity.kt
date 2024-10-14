package com.tasty.recipesapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tasty.recipesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: MainActivity created.")

        val inputText = intent.getStringExtra("editText")

        // Display the text in the TextView
        binding.textView.text = inputText
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: MainActivity started.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: MainActivity resumed.")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: MainActivity paused.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: MainActivity stopped.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: MainActivity destroyed.")
    }
}