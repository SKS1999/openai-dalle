package com.webomax.openai.presentation

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionGroupInfo
import android.location.SettingInjectorService
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore.Audio.Genres
import android.provider.Settings
import android.util.Log
import android.webkit.PermissionRequest
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.webomax.openai.*
import com.webomax.openai.data.model.GeneratedImage
import com.webomax.openai.databinding.ActivityMainBinding
import com.webomax.openai.presentation.generate_image.GenerateImageFragment
import com.webomax.openai.presentation.generate_image.GenerateImageFragment_GeneratedInjector
import com.webomax.openai.presentation.generate_image.GenerateImageViewModel
import com.webomax.openai.presentation.image_detail.ImageDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE = 200
    private lateinit var binding: ActivityMainBinding
    lateinit var btn_logout:ImageButton
    private lateinit var permissionLancher :ActivityResultLauncher<Array<String>>
    private var isReadPermissionGranted = false
    private lateinit var mAuth: FirebaseAuth
    lateinit var btn_profile:ImageButton
    lateinit var home:BottomNavigationItemView
    lateinit var recent: BottomNavigationItemView
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

            startActivity(Intent(this@MainActivity,loginActivity::class.java))
            finish()
        }
        btn_profile.setOnClickListener{
            startActivity(Intent(this,DashboardActivity::class.java))
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