package com.webomax.openai.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable.ProgressDrawableSize
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.webomax.openai.Profile.DashboardActivity
import com.webomax.openai.Profile.loginActivity
import com.webomax.openai.RecentFiles.RecentActivity
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.button.MaterialButton
import com.webomax.openai.R
import com.webomax.openai.presentation.generate_image.GenerateImageFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private final var TAG = "MainActivity"
    private var mRewardedAd: RewardedAd ?= null


    lateinit var btn_logout:ImageButton
    private lateinit var permissionLancher :ActivityResultLauncher<Array<String>>
    private var isReadPermissionGranted = false
    private lateinit var mAuth: FirebaseAuth
    lateinit var btn_profile:ImageButton

    lateinit var home:BottomNavigationItemView
    lateinit var recent :BottomNavigationItemView
    private var rewardedAd_btn : MaterialButton  ?=null

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        rewardedAd_btn = findViewById(R.id.rewardAd_btn)
        home= findViewById(R.id.home)
        recent= findViewById(R.id.recent)
        btn_logout = findViewById<ImageButton>(R.id.logout)
        btn_profile= findViewById(R.id.profile)

        permissionLancher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            isReadPermissionGranted = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE]?: isReadPermissionGranted
        }
        requestPermission()

        home.setOnClickListener {
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    finish()

                }
        recent.setOnClickListener {

                    startActivity(Intent(this@MainActivity, RecentActivity::class.java))
                     finish()
                }


       btn_logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@MainActivity, loginActivity::class.java))
            finish()
        }
        btn_profile.setOnClickListener{
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }





        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        //for splash screen

//        Handler().postDelayed({
//            if(user !=null){
//                val DashboardIntent =  Intent(this, DashboardActivity::class.java)
//                startActivity(DashboardIntent)
//                finish()
//            }
//            else{
//                val loginIntent = Intent(this,loginActivity::class.java)
//                startActivity(loginIntent)
//                finish()
//            }
//        },20)
    }
    private fun requestPermission(){
        isReadPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )==PackageManager.PERMISSION_GRANTED

        val PermissionRequest :MutableList<String> = ArrayList()
        if (!isReadPermissionGranted){
            PermissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (PermissionRequest.isNotEmpty()){
            permissionLancher.launch(PermissionRequest.toTypedArray())
        }
    }






}