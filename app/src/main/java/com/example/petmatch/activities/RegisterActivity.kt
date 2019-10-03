package com.example.petmatch.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.petmatch.R
import com.example.petmatch.utilities.DATA_USERS
import com.example.petmatch.utilities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDb = FirebaseDatabase.getInstance().reference
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
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
        fun registerIntent(context: Context?) = Intent(context, RegisterActivity::class.java)
    }


    fun onSignup(v: View) {
        val email = register_user_et.text.toString()
        val password = register_password_et.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid ?:""
                    val user = User(userId, "", email,"", "", "")
                    firebaseDb.child(DATA_USERS).child(userId).setValue(user)

                    startActivity(MatchActivity.mainIntent(this))
                } else {
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
