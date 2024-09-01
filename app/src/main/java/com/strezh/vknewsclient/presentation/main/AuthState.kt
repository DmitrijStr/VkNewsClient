package com.strezh.vknewsclient.presentation.main

sealed class AuthState {
    object Authorized: AuthState()
    object NonAuthorized: AuthState()
    object Initial: AuthState()
}