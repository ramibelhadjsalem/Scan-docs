package com.scandoc.presentation.navigation

import com.scandoc.presentation.camera.CameraEffect
import com.scandoc.presentation.crop.CropEffect
import com.scandoc.presentation.library.LibraryEffect
import com.scandoc.presentation.viewer.ViewerEffect
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class AppViewModelTest {
    @Test
    fun appViewModelOwnsScanFlowNavigation() = runTest {
        val viewModel = AppViewModel()

        viewModel.onSplashFinished()
        assertEquals(AppRoute.Library, viewModel.state.value.route)

        viewModel.onLibraryEffect(LibraryEffect.NavigateToCamera)
        assertEquals(AppRoute.Camera, viewModel.state.value.route)

        viewModel.onCameraEffect(CameraEffect.NavigateToCrop(byteArrayOf(7, 8)))
        assertEquals(AppRoute.Crop(byteArrayOf(7, 8)), viewModel.state.value.route)

        viewModel.onCropEffect(CropEffect.NavigateToViewer("doc-1"))
        assertEquals(AppRoute.Viewer("doc-1"), viewModel.state.value.route)

        viewModel.onViewerEffect(ViewerEffect.NavigateBack)
        assertEquals(AppRoute.Camera, viewModel.state.value.route)
    }
}
