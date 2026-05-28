package com.scandoc

import androidx.compose.runtime.Composable
import com.scandoc.presentation.navigation.RootComponent
import com.scandoc.presentation.navigation.RootContent
import com.scandoc.presentation.theme.ScanDocTheme

@Composable
fun App(rootComponent: RootComponent) {
    ScanDocTheme {
        RootContent(component = rootComponent)
    }
}
