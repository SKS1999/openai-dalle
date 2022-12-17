package com.webomax.openai.di


import com.webomax.openai.data.repository.DallERepositoryImpl
import com.webomax.openai.domain.repository.DallERepository
import com.webomax.openai.domain.source.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDallERepository(
        remoteDataSource: RemoteDataSource
    ): DallERepository =
        DallERepositoryImpl(remoteDataSource)
}