package com.example.petmatch.interfaces

import com.google.firebase.database.DatabaseReference

interface MatchCallback {
    fun onSignout()
    fun onGetUserId(): String
    fun getUserDb(): DatabaseReference
    fun profileComplete()
}