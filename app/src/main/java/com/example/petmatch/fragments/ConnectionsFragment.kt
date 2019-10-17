package com.example.petmatch.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.petmatch.R
import com.example.petmatch.interfaces.MatchCallback
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_connections.*


class ConnectionsFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDb: DatabaseReference
    private lateinit var chatDb: DatabaseReference
    private var callback: MatchCallback? = null
    private var

    fun setCallback(callback: MatchCallback){
        this.callback = callback
        userId = callback.onGetUserId()
        userDb = callback.getUserDb()
        chatDb = callback.getChatDb()

        fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connections_rv.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun fetchData(){


    }


}
