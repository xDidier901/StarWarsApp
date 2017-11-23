package com.example.didiervaldez.starwarsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import android.content.Intent
import android.support.annotation.NonNull
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.internal.zzdwf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUserMetadata
import com.google.firebase.auth.UserInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class Home : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser

        userName.text = user!!.email.toString()
        toast(user!!.displayName.toString())

        Picasso.with(this)
                .load(user!!.photoUrl)
                .into(profileImage)


//        println(obj.displayName)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.log_out -> {
                mAuth!!.signOut()
                toast(R.string.log_out_text)
                startActivity<MainActivity>()
            }
            R.id.about_us -> startActivity<AboutUs>()
            else -> { // Note the block
                print("x is neither 1 nor 2")
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
