package com.scandoc

import androidx.compose.ui.window.ComposeUIViewController
import com.scandoc.core.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    startKoin {
        modules(appModule)
    }
    App()
}
