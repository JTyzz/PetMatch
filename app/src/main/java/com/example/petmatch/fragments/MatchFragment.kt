package com.example.petmatch.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast

import com.example.petmatch.R
import com.example.petmatch.adapters.CardsAdapter
import com.example.petmatch.interfaces.MatchCallback
import com.example.petmatch.utilities.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.fragment_match.*


class MatchFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDb: DatabaseReference
    private var cardsAdapter: ArrayAdapter<User>? = null
    private var rowItems = ArrayList<User>()
    private var callback: MatchCallback? = null
    private var animals: String? = null

    fun setCallback(callback: MatchCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDb = callback.getUserDb()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDb.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                animals = user?.animals
                populateItems()
            }

        })

        yes_btn.setOnClickListener{
            frame.topCardListener.selectRight()
        }

        no_btn.setOnClickListener{
            frame.topCardListener.selectLeft()
        }

        frame.setOnItemClickListener{ position, data ->}

        cardsAdapter = CardsAdapter(context, R.layout.item, rowItems)
        frame.adapter = cardsAdapter
        frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                rowItems.removeAt(0)
                cardsAdapter?.notifyDataSetChanged()

            }

            override fun onLeftCardExit(p0: Any?) {
                var user = p0 as User
                userDb.child(user.uid.toString()).child(DATA_SWIPES_LEFT).child(userId).setValue(true)

            }

            override fun onRightCardExit(p0: Any?) {
                var selectedUser = p0 as User
                val selectedUserId = selectedUser.uid
                if(!selectedUserId.isNullOrEmpty()){
                    userDb.child(userId).child(DATA_SWIPES_RIGHT).addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.hasChild(selectedUserId)){
                                Toast.makeText(context, "Match found", Toast.LENGTH_SHORT).show()

                                userDb.child(userId).child(DATA_SWIPES_RIGHT).child(selectedUserId).removeValue()
                                userDb.child(userId).child(DATA_MATCHES).child(selectedUserId).setValue(true)
                                userDb.child(selectedUserId).child(DATA_MATCHES).child(userId).setValue(true)
                            } else {
                                userDb.child(selectedUserId).child(DATA_SWIPES_RIGHT).child(userId).setValue(true)
                            }
                        }

                    })
                }

            }

            override fun onAdapterAboutToEmpty(p0: Int) {

            }

            override fun onScroll(p0: Float) {

            }

        })
    }

    fun populateItems() {
        emptylist_layout.visibility = View.GONE
        pb_layout2.visibility = View.VISIBLE
        val cardsQuery = userDb.orderByChild(DATA_ANIMALS).equalTo(animals)
        cardsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach { child ->
                    val user = child.getValue(User::class.java)
                    if (user != null) {
                        var showUser = true
                        if (child.child(DATA_SWIPES_LEFT).hasChild(userId) && child.child(
                                DATA_SWIPES_RIGHT
                            ).hasChild(userId) && child.child(DATA_MATCHES).hasChild(userId)
                        ) {
                            showUser = false
                        }
                        if (showUser) {
                            rowItems.add(user)
                            cardsAdapter?.notifyDataSetChanged()
                        }
                    }
                }
                pb_layout2.visibility = View.GONE
                if(rowItems.isEmpty()){
                    emptylist_layout.visibility = View.VISIBLE
                }
            }

        })
    }


}
