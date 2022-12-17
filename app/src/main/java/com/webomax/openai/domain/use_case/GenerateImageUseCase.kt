package com.webomax.openai.domain.use_case


import com.webomax.openai.data.model.RequestBody
import com.webomax.openai.domain.repository.DallERepository
import javax.inject.Inject

class GenerateImageUseCase @Inject constructor(private val repository: DallERepository) {
    operator fun invoke(requestBody: RequestBody) = repository.generateImage(requestBody)
}