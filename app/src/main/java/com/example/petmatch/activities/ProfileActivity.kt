package com.example.petmatch.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.petmatch.R
import com.example.petmatch.utilities.DATA_USERS
import com.example.petmatch.utilities.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userId = intent.extras.getString(USER_ID_PARAM, "")
        if(userId.isNullOrEmpty()) {
            finish()
        }

        val userDatabase = FirebaseDatabase.getInstance().reference.child(DATA_USERS)
        userDatabase.child(userId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                userInfoName.text = user?.name
                if(user?.imageUrl != null) {
                    Glide.with(this@ProfileActivity)
                        .load(user.imageUrl)
                        .into(userInfoIV)
                }
            }
        })
    }

    companion object {
        private val USER_ID_PARAM = "User id"

        fun newIntent(context: Context, userId: String?): Intent {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(USER_ID_PARAM, userId)
            return intent
        }
    }
}
