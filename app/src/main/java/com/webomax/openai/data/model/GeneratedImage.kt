package com.webomax.openai.data.model


import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GeneratedImage(
    @SerializedName("created")
    val created: Int,
    @SerializedName("data")
    val `data`: List<Data>


)
