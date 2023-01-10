package com.webomax.openai.adapter

import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.webomax.openai.R
import java.io.File

class MyAdapter (var items: Int): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    var item:Int = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.recent_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val path = File(Environment.DIRECTORY_PICTURES+ "/ImageAI")

        if (path.exists()) {
           var listItems = path.list()
            val bmp = BitmapFactory.decodeFile(path.listFiles()?.get(position)?.absolutePath)
            holder.image.setImageBitmap(bmp)
        }else{
            Log.e("Photos","No Files Found")
        }
    }

    override fun getItemCount(): Int {

        return item

    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val image: ImageView= itemView.findViewById(R.id.image_recent)
    }
}