package com.scandoc.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.scandoc.presentation.camera.CameraEffect
import com.scandoc.presentation.camera.CameraIntent
import com.scandoc.presentation.camera.CameraScreen
import com.scandoc.presentation.camera.CameraViewModel
import com.scandoc.presentation.crop.CropEffect
import com.scandoc.presentation.crop.CropIntent
import com.scandoc.presentation.crop.CropScreen
import com.scandoc.presentation.crop.CropViewModel
import com.scandoc.presentation.library.LibraryEffect
import com.scandoc.presentation.library.LibraryScreen
import com.scandoc.presentation.library.LibraryViewModel
import com.scandoc.presentation.viewer.ViewerEffect
import com.scandoc.presentation.viewer.ViewerIntent
import com.scandoc.presentation.viewer.ViewerScreen
import com.scandoc.presentation.viewer.ViewerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) { child ->
        when (val instance = child.instance) {
            RootComponent.Child.LibraryChild -> LibraryRoute(
                onNavigateToCamera = component::navigateToCamera,
                onNavigateToViewer = component::navigateToViewer,
            )
            RootComponent.Child.CameraChild -> CameraRoute(
                onNavigateToCrop = component::navigateToCrop,
                onNavigateBack = component::navigateBack,
            )
            is RootComponent.Child.CropChild -> CropRoute(
                imageBytes = instance.imageBytes,
                onNavigateToViewer = component::navigateToViewer,
                onNavigateBack = component::navigateBack,
            )
            is RootComponent.Child.ViewerChild -> ViewerRoute(
                documentId = instance.documentId,
                onNavigateBack = component::navigateBack,
            )
        }
    }
}

@Composable
private fun LibraryRoute(
    onNavigateToCamera: () -> Unit,
    onNavigateToViewer: (String) -> Unit,
) {
    val viewModel = koinViewModel<LibraryViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LibraryEffect.NavigateToCamera -> onNavigateToCamera()
                is LibraryEffect.NavigateToViewer -> onNavigateToViewer(effect.id)
                is LibraryEffect.ShowError -> Unit
            }
        }
    }

    LibraryScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun CameraRoute(
    onNavigateToCrop: (ByteArray) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<CameraViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onIntent(CameraIntent.StartCamera)
        viewModel.effects.collect { effect ->
            when (effect) {
                is CameraEffect.NavigateToCrop -> onNavigateToCrop(effect.imageBytes)
                is CameraEffect.ShowError -> Unit
            }
        }
    }

    CameraScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun CropRoute(
    imageBytes: ByteArray,
    onNavigateToViewer: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<CropViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(imageBytes) {
        viewModel.onIntent(CropIntent.SetImage(imageBytes))
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CropEffect.NavigateToViewer -> onNavigateToViewer(effect.documentId)
                CropEffect.NavigateBack -> onNavigateBack()
                is CropEffect.ShowError -> Unit
            }
        }
    }

    CropScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun ViewerRoute(
    documentId: String,
    onNavigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<ViewerViewModel>(key = documentId)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(documentId) {
        viewModel.onIntent(ViewerIntent.Load(documentId))
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ViewerEffect.ShareFile -> Unit
                is ViewerEffect.ShowError -> Unit
                ViewerEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    ViewerScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
