package com.webomax.openai.data.model


import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: Any,
    @SerializedName("message")
    val message: String,
    @SerializedName("param")
    val `param`: Any,
    @SerializedName("type")
    val type: String
)