package com.webomax.openai.data.repository


import com.webomax.openai.common.Resource
import com.webomax.openai.data.model.RequestBody
import com.webomax.openai.domain.repository.DallERepository
import com.webomax.openai.domain.source.RemoteDataSource
import kotlinx.coroutines.flow.flow

class DallERepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : DallERepository {
    override fun generateImage(
        requestBody: RequestBody
    ) = flow {
        emit(Resource.Loading)

        try {
            val response = remoteDataSource.generateImage(requestBody)
            emit(Resource.Success(response))
        } catch (t: Throwable) {
            emit(Resource.Error(t))
        }

    }

}