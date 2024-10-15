package com.strezh.vknewsclient.di

import android.content.Context
import com.strezh.vknewsclient.data.network.ApiFactory
import com.strezh.vknewsclient.data.network.ApiService
import com.strezh.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.strezh.vknewsclient.domain.repository.NewsFeedRepository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository


    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun provideVkStorage(context: Context): VKPreferencesKeyValueStorage {
            return VKPreferencesKeyValueStorage(context)
        }
    }
}