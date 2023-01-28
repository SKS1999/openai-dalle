package com.webomax.openai.RecentFiles

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.webomax.openai.Profile.DashboardActivity
import com.webomax.openai.R
import com.webomax.openai.adapter.MyAdapter
import com.webomax.openai.presentation.MainActivity
import java.io.File


class RecentActivity : AppCompatActivity() {
    private lateinit var home: BottomNavigationItemView
    private lateinit var recent: BottomNavigationItemView
    private lateinit var profile:BottomNavigationItemView
    var URI_LIST_DATA = "URI_LIST_DATA"
    var IMAGE_FULL_SCREEN_CURRENT_POS = "IMAGE_FULL_SCREEN_CURRENT_POS"
    private var listItems=1
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent)

        home= findViewById(R.id.home)
        recent= findViewById(R.id.recent)
        profile = findViewById(R.id.Profile)
        initView()
        home.setOnClickListener {
            startActivity(Intent(this@RecentActivity, MainActivity::class.java))

        }
        recent.setOnClickListener {
            startActivity(Intent(this@RecentActivity, RecentActivity::class.java))

        }
        profile.setOnClickListener {
            startActivity(Intent(this@RecentActivity, DashboardActivity::class.java))

        }


    }  //TODO("ADD A FULLSCREEN VIEW")
    private fun initView() {
        val recyclerView = findViewById<RecyclerView>(R.id.gridview)
        recyclerView.adapter = MyAdapter(getItemSize())
        recyclerView.layoutManager = GridLayoutManager(this, 2)



    }


    //TODO(NO ITEMS ARE SHOWING INSIDE THE ACTIVITY)
    private fun getItemSize():Int{
        val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"/ImageAI/")
        if (path.exists()) {
            listItems = path.list().size
        }
        return listItems

    }


}