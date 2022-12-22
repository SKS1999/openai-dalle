package com.webomax.openai



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView


class RecentActivity : AppCompatActivity() {
    lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent)

          gridView = findViewById<GridView>(R.id.gridview)

    }






}