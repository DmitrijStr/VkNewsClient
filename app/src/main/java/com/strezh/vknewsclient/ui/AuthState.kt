package com.strezh.vknewsclient.ui

sealed class AuthState {
    object Authorized: AuthState()
    object NonAuthorized: AuthState()
    object Initial: AuthState()
}