package com.webomax.openai

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.webomax.openai.adapter.MyAdapter
import com.webomax.openai.presentation.MainActivity
import java.io.File

@Suppress("UNREACHABLE_CODE")
class RecentActivity : AppCompatActivity() {
    lateinit var home: BottomNavigationItemView
    lateinit var recent: BottomNavigationItemView
    private var listItems=0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent)
        home= findViewById(R.id.home)
        recent= findViewById(R.id.recent)
        initView()
        home.setOnClickListener {
            startActivity(Intent(this@RecentActivity, MainActivity::class.java))
            finish()

        }
        recent.setOnClickListener {
            startActivity(Intent(this@RecentActivity, RecentActivity::class.java))
            finish()
        }
    }
    private fun initView() {
        val recyclerView = findViewById<RecyclerView>(R.id.gridview)
        recyclerView.adapter = MyAdapter(getItemSize())
        recyclerView.layoutManager = GridLayoutManager(this,2)
    }


    private fun getItemSize():Int{
        val path = File(Environment.getExternalStorageDirectory(), "/ImageAI")

        if (path.exists()) {
            listItems = path.list().size
        }
        return listItems



    }
}