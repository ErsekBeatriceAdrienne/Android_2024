package com.tasty.recipesapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.tasty.recipesapp.R

class GoogleAuthFragment : Fragment() {
    companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GoogleAuthFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(getString(R.string.server_client_id))
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                saveTokenToSharedPrefs(idToken)
                Toast.makeText(requireContext(), "Authentication successful!", Toast.LENGTH_SHORT).show()
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign-in failed", e)
            }
        }
    }

    private fun saveTokenToSharedPrefs(token: String?) {
        val sharedPreferences = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }
}