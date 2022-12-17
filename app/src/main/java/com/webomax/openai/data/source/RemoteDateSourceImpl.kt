package com.webomax.openai.data.source


import com.webomax.openai.data.model.GeneratedImage
import com.webomax.openai.data.model.RequestBody
import com.webomax.openai.domain.source.RemoteDataSource

class RemoteDateSourceImpl(private val remoteService: DallEService) : RemoteDataSource {
    override suspend fun generateImage(
        requestBody: RequestBody
    ): GeneratedImage {
        return remoteService.generateImage(requestBody)
    }
}