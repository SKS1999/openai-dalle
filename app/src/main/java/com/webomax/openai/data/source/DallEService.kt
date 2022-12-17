package com.webomax.openai.data.source


import com.webomax.openai.common.Constants.GENERATE_IMAGE
import com.webomax.openai.data.model.GeneratedImage
import com.webomax.openai.data.model.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface DallEService {

    @POST(GENERATE_IMAGE)
    suspend fun generateImage(@Body body: RequestBody): GeneratedImage

}