package com.scandoc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold
import com.scandoc.presentation.camera.CameraIntent
import com.scandoc.presentation.camera.CameraScreen
import com.scandoc.presentation.camera.CameraViewModel
import com.scandoc.presentation.crop.CropIntent
import com.scandoc.presentation.crop.CropScreen
import com.scandoc.presentation.crop.CropViewModel
import com.scandoc.presentation.library.LibraryScreen
import com.scandoc.presentation.library.LibraryViewModel
import com.scandoc.presentation.me.MeScreen
import com.scandoc.presentation.navigation.AppRoute
import com.scandoc.presentation.navigation.AppViewModel
import com.scandoc.presentation.navigation.NavTab
import com.scandoc.presentation.navigation.ScanDocBottomBar
import com.scandoc.presentation.splash.SplashScreen
import com.scandoc.presentation.theme.ScanDocTheme
import com.scandoc.presentation.tools.ToolsScreen
import com.scandoc.presentation.tools.ToolsViewModel
import com.scandoc.presentation.viewer.ViewerIntent
import com.scandoc.presentation.viewer.ViewerScreen
import com.scandoc.presentation.viewer.ViewerViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    ScanDocTheme {
        val appViewModel = koinViewModel<AppViewModel>()
        val appState by appViewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            delay(1_100)
            appViewModel.onSplashFinished()
        }

        Box(Modifier.fillMaxSize()) {
            if (appState.route == AppRoute.Splash) {
                SplashScreen(onContinue = appViewModel::onSplashFinished)
            } else {
                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        ScanDocBottomBar(
                            selectedTab = appState.selectedTab,
                            onTabSelected = appViewModel::selectTab,
                            onCameraClick = appViewModel::openCamera,
                        )
                    },
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        when (appState.selectedTab) {
                            NavTab.Home -> HomeTabContent(appViewModel)
                            NavTab.Files -> FilesTabContent(appViewModel)
                            NavTab.Tools -> ToolsTabContent()
                            NavTab.Me -> MeScreen()
                        }
                    }
                }

                AnimatedVisibility(
                    visible = appState.isCameraOverlayOpen,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    CameraTabContent(appViewModel)
                }

                when (val route = appState.route) {
                    is AppRoute.Crop -> CropTabContent(appViewModel, route)
                    is AppRoute.Viewer -> ViewerTabContent(appViewModel, route)
                    AppRoute.Camera -> CameraTabContent(appViewModel)
                    AppRoute.Home,
                    AppRoute.Library,
                    AppRoute.Me,
                    AppRoute.Splash,
                    AppRoute.Tools,
                    -> Unit
                }
            }
        }
    }
}

@Composable
private fun HomeTabContent(appViewModel: AppViewModel) {
    LibraryTabContent(appViewModel, isHomeMode = true)
}

@Composable
private fun FilesTabContent(appViewModel: AppViewModel) {
    LibraryTabContent(appViewModel, isHomeMode = false)
}

@Composable
private fun LibraryTabContent(
    appViewModel: AppViewModel,
    isHomeMode: Boolean,
) {
    val viewModel = koinViewModel<LibraryViewModel>()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            appViewModel.onLibraryEffect(effect)
        }
    }
    LibraryScreen(
        state = state,
        onIntent = viewModel::onIntent,
        isHomeMode = isHomeMode,
    )
}

@Composable
private fun ToolsTabContent() {
    val viewModel = koinViewModel<ToolsViewModel>()
    val state by viewModel.state.collectAsState()
    ToolsScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun CameraTabContent(appViewModel: AppViewModel) {
    val viewModel = koinViewModel<CameraViewModel>()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.onIntent(CameraIntent.StartCamera)
        viewModel.effects.collect { effect ->
            appViewModel.onCameraEffect(effect)
        }
    }
    CameraScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onClose = {
            viewModel.onIntent(CameraIntent.StopCamera)
            appViewModel.closeCamera()
        },
    )
}

@Composable
private fun CropTabContent(
    appViewModel: AppViewModel,
    route: AppRoute.Crop,
) {
    val viewModel = koinViewModel<CropViewModel>()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(viewModel, route.imageBytes) {
        viewModel.onIntent(CropIntent.SetImage(route.imageBytes))
        viewModel.effects.collect { effect ->
            appViewModel.onCropEffect(effect)
        }
    }
    CropScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun ViewerTabContent(
    appViewModel: AppViewModel,
    route: AppRoute.Viewer,
) {
    val viewModel = koinViewModel<ViewerViewModel>()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(viewModel, route.documentId) {
        viewModel.onIntent(ViewerIntent.Load(route.documentId))
        viewModel.effects.collect { effect ->
            appViewModel.onViewerEffect(effect)
        }
    }
    ViewerScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
