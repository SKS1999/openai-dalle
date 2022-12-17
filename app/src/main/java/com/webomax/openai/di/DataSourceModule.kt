package com.webomax.openai.di


import com.webomax.openai.data.source.DallEService
import com.webomax.openai.data.source.RemoteDateSourceImpl
import com.webomax.openai.domain.source.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDateSource(remoteService: DallEService): RemoteDataSource =
        RemoteDateSourceImpl(remoteService)
}