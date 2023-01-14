package com.webomax.openai.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.webomax.openai.Profile.DashboardActivity
import com.webomax.openai.R
import com.webomax.openai.RecentFiles.RecentActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private final var TAG = "MainActivity"



    private lateinit var permissionLancher :ActivityResultLauncher<Array<String>>
    private var isReadPermissionGranted = false
    private lateinit var mAuth: FirebaseAuth
    lateinit var btn_profile:BottomNavigationItemView
    lateinit var home:BottomNavigationItemView
    lateinit var recent :BottomNavigationItemView

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        home= findViewById(R.id.home)
        recent= findViewById(R.id.recent)
        btn_profile= findViewById(R.id.Profile)

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

        btn_profile.setOnClickListener{
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()

        }






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