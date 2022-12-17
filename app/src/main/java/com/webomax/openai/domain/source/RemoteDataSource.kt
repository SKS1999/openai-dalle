package com.webomax.openai.domain.source


import com.webomax.openai.data.model.GeneratedImage
import com.webomax.openai.data.model.RequestBody

interface RemoteDataSource {
    suspend fun generateImage(requestBody: RequestBody): GeneratedImage
}