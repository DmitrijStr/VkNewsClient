package com.strezh.vknewsclient.di

import com.strezh.vknewsclient.domain.entity.FeedPost
import com.strezh.vknewsclient.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [CommentsViewModelModule::class])
interface CommentsScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance feedPost: FeedPost): CommentsScreenComponent
    }
}