package com.webomax.openai.data.model



import com.google.gson.annotations.SerializedName

data class GeneratedImage(
    @SerializedName("created")
    val created: Int,
    @SerializedName("data")
    val `data`: List<Data>


)
