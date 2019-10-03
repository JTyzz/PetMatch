package com.example.petmatch.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.petmatch.R
import com.example.petmatch.fragments.ConnectionsFragment
import com.example.petmatch.fragments.MatchFragment
import com.example.petmatch.fragments.ProfileFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_match.*

class MatchActivity : AppCompatActivity() {

    private var profileFragment: ProfileFragment? = null
    private var matchFragment: MatchFragment? = null
    private var connectionsFragment: ConnectionsFragment? = null

    private var profileTab: TabLayout.Tab? = null
    private var matchTab: TabLayout.Tab? = null
    private var connectionsTab: TabLayout.Tab? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        profileTab = nav_tabs.newTab()
        matchTab = nav_tabs.newTab()
        connectionsTab = nav_tabs.newTab()

        profileTab?.icon = ContextCompat.getDrawable(this, R.drawable.tab_profile)
        matchTab?.icon = ContextCompat.getDrawable(this, R.drawable.tab_match)
        connectionsTab?.icon = ContextCompat.getDrawable(this, R.drawable.tab_connections)

        nav_tabs.addTab(profileTab!!)
        nav_tabs.addTab(matchTab!!)
        nav_tabs.addTab(connectionsTab!!)

    }
    companion object {
        fun mainIntent(context: Context?)= Intent(context, MatchActivity::class.java)
    }
}
