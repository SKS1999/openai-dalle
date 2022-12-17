package com.webomax.openai.domain.repository



import com.webomax.openai.common.Resource
import com.webomax.openai.data.model.GeneratedImage
import com.webomax.openai.data.model.RequestBody
import kotlinx.coroutines.flow.Flow

interface DallERepository {
    fun generateImage(requestBody: RequestBody): Flow<Resource<GeneratedImage>>
}