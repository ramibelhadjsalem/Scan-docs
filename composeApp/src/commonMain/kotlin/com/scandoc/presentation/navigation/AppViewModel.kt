package com.scandoc.presentation.navigation

import com.scandoc.presentation.base.BaseViewModel
import com.scandoc.presentation.camera.CameraEffect
import com.scandoc.presentation.crop.CropEffect
import com.scandoc.presentation.library.LibraryEffect
import com.scandoc.presentation.viewer.ViewerEffect

class AppViewModel : BaseViewModel<AppState, AppEffect>(AppState()) {

    fun onSplashFinished() {
        if (currentState.route == AppRoute.Splash) {
            replace(AppRoute.Home)
        }
    }

    fun onLibraryEffect(effect: LibraryEffect) {
        when (effect) {
            LibraryEffect.NavigateToCamera -> openCamera()
            is LibraryEffect.NavigateToViewer -> navigate(AppRoute.Viewer(effect.id))
            is LibraryEffect.ShowError -> Unit
        }
    }

    fun onCameraEffect(effect: CameraEffect) {
        when (effect) {
            is CameraEffect.NavigateToCrop -> {
                closeCamera()
                navigate(AppRoute.Crop(effect.imageBytes))
            }

            is CameraEffect.ShowError -> Unit
        }
    }

    fun onCropEffect(effect: CropEffect) {
        when (effect) {
            CropEffect.NavigateBack -> back()
            CropEffect.NavigateToCamera -> navigate(AppRoute.Camera)
            is CropEffect.NavigateToViewer -> replace(AppRoute.Viewer(effect.documentId))
            is CropEffect.ShowError -> Unit
        }
    }

    fun onViewerEffect(effect: ViewerEffect) {
        when (effect) {
            ViewerEffect.NavigateBack -> back()
            is ViewerEffect.ShareFile -> Unit
            is ViewerEffect.ShowError -> Unit
        }
    }

    fun selectTab(tab: NavTab) {
        val route = when (tab) {
            NavTab.Home -> AppRoute.Home
            NavTab.Files -> AppRoute.Library
            NavTab.Tools -> AppRoute.Tools
            NavTab.Me -> AppRoute.Me
        }
        updateState { state ->
            state.copy(
                route = route,
                backStack = listOf(route),
                selectedTab = tab,
                isCameraOverlayOpen = false,
            )
        }
    }

    fun openCamera() {
        updateState { it.copy(isCameraOverlayOpen = true) }
    }

    fun closeCamera() {
        updateState { it.copy(isCameraOverlayOpen = false) }
    }

    private fun navigate(route: AppRoute) {
        updateState { state ->
            state.copy(
                route = route,
                backStack = state.backStack + route,
            )
        }
    }

    private fun replace(route: AppRoute) {
        updateState { state ->
            val stack = state.backStack
            val nextStack = if (stack.isEmpty()) {
                listOf(route)
            } else {
                stack.dropLast(1) + route
            }
            state.copy(route = route, backStack = nextStack)
        }
    }

    private fun back() {
        updateState { state ->
            if (state.backStack.size <= 1) {
                AppState(route = AppRoute.Home, backStack = listOf(AppRoute.Home))
            } else {
                val nextStack = state.backStack.dropLast(1)
                state.copy(route = nextStack.last(), backStack = nextStack)
            }
        }
    }
}
