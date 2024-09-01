package com.strezh.vknewsclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import com.strezh.vknewsclient.ui.MainScreen
import com.strezh.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            VkNewsClientTheme {
                val authLauncher =
                    rememberLauncherForActivityResult(contract = VK.getVKAuthActivityResultContract()) { result ->
                        when (result) {
                            is VKAuthenticationResult.Success -> {
                                Log.d("MainActivity", "Success auth")
                            }

                            is VKAuthenticationResult.Failed -> {
                                Log.d("MainActivity", "Failed auth")

                            }
                        }
                    }

                SideEffect {
                    authLauncher.launch(listOf(VKScope.WALL))
                }



                MainScreen()
            }
        }
    }
}
