package com.example.petmatch.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.petmatch.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener { firebaseAuthListener }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener { firebaseAuthListener }
    }

    companion object {
        fun loginIntent(context: Context?) = Intent(context, StartActivity::class.java)
    }

    fun onLogin(v: View) {
        val email = start_user_et.text.toString()
        val password = start_password_et.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(MatchActivity.mainIntent(this))
                    } else {
                        Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    fun onRegister(v: View) {
        startActivity(RegisterActivity.registerIntent(this))
    }
}
