package com.strezh.vknewsclient.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.strezh.vknewsclient.domain.entity.AuthState
import com.strezh.vknewsclient.presentation.getApplicationComponent
import com.strezh.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Log.d("RECOMPOSITION", "MainActivity")
            val component = getApplicationComponent()
            val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = viewModel.authState.collectAsState(AuthState.Initial)
            val authLauncher = rememberLauncherForActivityResult(
                contract = VK.getVKAuthActivityResultContract()
            ) {
                viewModel.performAuthResult()
            }

            VkNewsClientTheme {
                when (authState.value) {
                    is AuthState.Authorized -> {
                        MainScreen()
                    }

                    is AuthState.NonAuthorized -> {
                        LoginScreen(
                            onLoginClick = {
                                authLauncher.launch(
                                    listOf(
                                        VKScope.WALL,
                                        VKScope.GROUPS,
                                        VKScope.FRIENDS
                                    )
                                )
                            }
                        )
                    }

                    is AuthState.Initial -> {

                    }
                }

            }
        }
    }
}
