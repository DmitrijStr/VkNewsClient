package com.strezh.vknewsclient.domain.entity

sealed class AuthState {
    object Authorized: AuthState()
    object NonAuthorized: AuthState()
    object Initial: AuthState()
}