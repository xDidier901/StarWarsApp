package com.example.didiervaldez.starwarsapp

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import com.google.gson.Gson
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {


    private var mediaPlayer: MediaPlayer? = null
    private var sound: Boolean = true
    private var songTime: Int = 0
    private var videoTime: Int = 0


    //GoogleSignIn
    private val RC_SIGN_IN = 9001
    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        soundButton.setImageResource(R.drawable.sound)
        initiateBackground()
        setSoundListener()

        startGoogleConfig()
        setGoogleListener()

    }

    private fun setGoogleListener() {
        signInButton.setOnClickListener({
            signIn()
        })
    }

    private fun startGoogleConfig() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun initiateBackground() {

        //Initialize background video
        val videoUri: Uri = Uri.parse("android.resource://$packageName/${R.raw.background_video}")
        bgVideo.setVideoURI(videoUri)
        bgVideo.start()
        bgVideo.setOnPreparedListener({ player ->
            player.isLooping = true
        })

        //Star Wars Main Theme Song
        val songUri: Uri = Uri.parse("android.resource://$packageName/${R.raw.star_wars_intro_song}")
        mediaPlayer = MediaPlayer.create(applicationContext, songUri)
        mediaPlayer!!.start()
        mediaPlayer!!.setOnPreparedListener({ player ->
            player.isLooping = true
            animateLogo()
        })

    }

    private fun setSoundListener() {
        soundButton.setOnClickListener({
            if (sound) {
                mediaPlayer!!.setVolume(0f, 0f)
                sound = false
                soundButton.setImageResource(R.drawable.mute)
            } else {
                mediaPlayer!!.setVolume(1f, 1f)
                sound = true
                soundButton.setImageResource(R.drawable.sound)
            }
        })
    }

    private fun animateLogo() {
        logoView.setImageResource(R.mipmap.star_wars_logo)
        logoView.scaleX = .01f
        logoView.scaleY = .01f
        logoView.animate()!!
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(4000)
                .setInterpolator(DecelerateInterpolator())
                .start()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                updateUI(null)
            }

        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth!!.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        toast(R.string.login_error)
                        updateUI(null)
                    }
                }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        // Firebase sign out
        mAuth!!.signOut()

        // Google sign out
        mGoogleSignInClient!!.signOut().addOnCompleteListener(this
        ) { updateUI(null) }
    }

    private fun revokeAccess() {
        // Firebase sign out
        mAuth!!.signOut()

        // Google revoke access
        mGoogleSignInClient!!.revokeAccess().addOnCompleteListener(this
        ) { updateUI(null) }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            toast(R.string.login_succesful)
            startActivity<Home>()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    override fun onPause() {
        super.onPause()
        songTime = mediaPlayer!!.currentPosition
        mediaPlayer!!.pause()
        videoTime = bgVideo.currentPosition
        bgVideo.pause()
    }


    override fun onResume() {
        super.onResume()
        mediaPlayer!!.seekTo(songTime)
        mediaPlayer!!.start()
        bgVideo.seekTo(videoTime)
        bgVideo.start()
    }


}
