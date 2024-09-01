package com.strezh.vknewsclient.presentation.main

import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Initial)
    val authState = _authState.asStateFlow()

    init {
        _authState.value = if (VK.isLoggedIn()) AuthState.Authorized else AuthState.NonAuthorized
    }

    fun performAuthResult(result: VKAuthenticationResult) {
        if (result is VKAuthenticationResult.Success) {
            _authState.value = AuthState.Authorized
        } else {
            _authState.value = AuthState.NonAuthorized
        }
    }
}