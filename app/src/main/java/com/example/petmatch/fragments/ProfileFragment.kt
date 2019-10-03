package com.example.petmatch.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

import com.example.petmatch.R
import com.example.petmatch.interfaces.MatchCallback
import com.example.petmatch.utilities.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDb: DatabaseReference
    private var callback: MatchCallback? = null

    fun setCallback(callback: MatchCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDb = callback.getUserDb().child(userId)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pb_layout.setOnTouchListener { view, event -> true }

        populateInfo()

        user_photo_iv.setOnClickListener{ callback?.startActivityForPhoto()}

        applyButton.setOnClickListener { onApply() }

    }

    fun populateInfo() {
        pb_layout.visibility = View.VISIBLE
        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                pb_layout.visibility = View.GONE
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (isAdded) {
                    val user = p0.getValue(User::class.java)
                    nameET.setText(user?.name, TextView.BufferType.EDITABLE)
                    emailET.setText(user?.email, TextView.BufferType.EDITABLE)
                    if (user?.animals == "dog") {
                        radio_dog.isChecked = true
                    } else {
                        radio_cat.isChecked = true
                    }
                    if (user?.imageUrl!!.isNotEmpty()){
                        populateImage(user.imageUrl)
                    }
                    pb_layout.visibility = View.GONE
                }
            }

        })
    }

    fun onApply() {
        val name = nameET.text.toString()
        val email = emailET.text.toString()
        val animalPreference = if (radio_dog.isChecked) "dog"
                                                   else "cat"

        if (nameET.text.toString().isNullOrEmpty()) {
            Toast.makeText(context, "Please enter your name.", Toast.LENGTH_SHORT)
        } else {
            userDb.child(DATA_NAME).setValue(name)
            userDb.child(DATA_EMAIL).setValue(email)
            userDb.child(DATA_ANIMALS).setValue(animalPreference)

            callback?.profileComplete()
        }

    }
    fun updateImageUri(uri: String) {
        userDb.child(DATA_IMAGE_URL).setValue(uri)
        populateImage(uri)
    }

    fun populateImage(uri: String){
        Glide.with(this).load(uri).into(user_photo_iv)
    }
}
