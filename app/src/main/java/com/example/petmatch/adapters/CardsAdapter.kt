package com.example.petmatch.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.petmatch.R
import com.example.petmatch.activities.ProfileActivity
import com.example.petmatch.utilities.User

class CardsAdapter(context: Context?, resourceId: Int, users: List<User>): ArrayAdapter<User>(context, resourceId, users) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var user = getItem(position)
        var finalView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item, parent, false)

        var name = finalView.findViewById<TextView>(R.id.name_tv)
        var image = finalView.findViewById<ImageView>(R.id.photo_iv)
        var userinfo = finalView.findViewById<LinearLayout>(R.id.profileClickLayout)

        name.text = user.name
        Glide.with(context).load(user.imageUrl).into(image)

        userinfo.setOnClickListener{
            finalView.context.startActivity(ProfileActivity.newIntent(finalView.context, user.uid))
        }

        return finalView
    }
}