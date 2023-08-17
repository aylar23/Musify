package com.musify.app.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * A Dagger Hilt module that provides an instance of [TrackService].
 * This module is installed in the [SingletonComponent], meaning that the provided [TrackService]
 * instance will be a singleton.
 */
@Module
@InstallIn(SingletonComponent::class)
class DataModule {


//    @Provides
//    @Singleton
//    fun provideTrackService(retrofit: Retrofit): TrackService {
//        return retrofit.create(TrackService::class.java)
//    }
}