package com.webomax.openai.data.model


import com.google.gson.annotations.SerializedName

data class GeneratedImageError(
    @SerializedName("error")
    val error: Error
)