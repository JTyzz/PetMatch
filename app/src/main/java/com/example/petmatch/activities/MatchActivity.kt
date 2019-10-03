package com.example.petmatch.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.petmatch.R
import com.example.petmatch.fragments.ConnectionsFragment
import com.example.petmatch.fragments.MatchFragment
import com.example.petmatch.fragments.ProfileFragment
import com.example.petmatch.interfaces.MatchCallback
import com.example.petmatch.utilities.DATA_USERS
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_match.*
import java.io.ByteArrayOutputStream
import java.io.IOException

const val REQUEST_CODE_PHOTO = 1234

class MatchActivity : AppCompatActivity(), MatchCallback {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid

    private lateinit var userDb: DatabaseReference

    private var profileFragment: ProfileFragment? = null
    private var matchFragment: MatchFragment? = null
    private var connectionsFragment: ConnectionsFragment? = null

    private var profileTab: TabLayout.Tab? = null
    private var matchTab: TabLayout.Tab? = null
    private var connectionsTab: TabLayout.Tab? = null

    private var resultImageUrl: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "No user!", Toast.LENGTH_SHORT).show()
            onSignout()
        }

        userDb = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        profileTab = nav_tabs.newTab()
        matchTab = nav_tabs.newTab()
        connectionsTab = nav_tabs.newTab()

        profileTab?.icon = ContextCompat.getDrawable(this, R.drawable.tab_profile)
        matchTab?.icon = ContextCompat.getDrawable(this, R.drawable.tab_match)
        connectionsTab?.icon = ContextCompat.getDrawable(this, R.drawable.tab_connections)

        nav_tabs.addTab(profileTab!!)
        nav_tabs.addTab(matchTab!!)
        nav_tabs.addTab(connectionsTab!!)

        nav_tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                onTabSelected(p0)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0) {
                    profileTab -> {
                        if (profileFragment == null) {
                            profileFragment = ProfileFragment()
                            profileFragment!!.setCallback(this@MatchActivity)
                        }
                        replaceFragment(profileFragment!!)
                    }
                    matchTab -> {
                        if (matchFragment == null) {
                            matchFragment = MatchFragment()
                            matchFragment!!.setCallback(this@MatchActivity)
                        }
                        replaceFragment(matchFragment!!)
                    }
                    connectionsTab -> {
                        if (connectionsFragment == null) {
                            connectionsFragment = ConnectionsFragment()
                            connectionsFragment!!.setCallback(this@MatchActivity)
                        }
                        replaceFragment(connectionsFragment!!)
                    }
                }
            }

        })

        profileTab?.select()

    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            resultImageUrl = data?.data
            storeImage()
        }
    }

    fun storeImage() {
        if (resultImageUrl != null && userId != null) {
            val filePath =
                FirebaseStorage.getInstance().reference.child("profileImage").child(userId)
            var bitmap: Bitmap? = null
            try {
                bitmap =
                    MediaStore.Images.Media.getBitmap(application.contentResolver, resultImageUrl)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 20, baos)
            val data = baos.toByteArray()

            val uploadTask = filePath.putBytes(data)
            uploadTask.addOnFailureListener { e -> e.printStackTrace() }
            uploadTask.addOnSuccessListener { taskSnapshot ->
                filePath.downloadUrl.addOnSuccessListener { uri ->
                    profileFragment?.updateImageUri(uri.toString())
                }
                    .addOnFailureListener { e -> e.printStackTrace() }
            }

        }
    }

    override fun onSignout() {
        firebaseAuth.signOut()
        startActivity(StartActivity.loginIntent(this))
    }

    override fun onGetUserId(): String = userId!!

    override fun getUserDb(): DatabaseReference = userDb

    override fun profileComplete() {
        matchTab?.select()
    }

    override fun startActivityForPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PHOTO)
    }

    companion object {
        fun mainIntent(context: Context?) = Intent(context, MatchActivity::class.java)
    }
}
