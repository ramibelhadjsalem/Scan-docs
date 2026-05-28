package com.scandoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.scandoc.presentation.navigation.DefaultRootComponent

class MainActivity : ComponentActivity() {

    private val rootComponent by lazy {
        DefaultRootComponent(componentContext = defaultComponentContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(rootComponent = rootComponent)
        }
    }
}
