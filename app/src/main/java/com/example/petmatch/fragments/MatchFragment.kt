package com.example.petmatch.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.petmatch.R
import com.example.petmatch.interfaces.MatchCallback
import com.google.firebase.database.DatabaseReference


class MatchFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDb: DatabaseReference
    private var callback: MatchCallback? = null

    fun setCallback(callback: MatchCallback){
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match, container, false)
    }


}
