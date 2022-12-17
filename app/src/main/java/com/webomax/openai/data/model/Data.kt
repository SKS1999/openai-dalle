package com.webomax.openai.data.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("url")
    val url: String
)