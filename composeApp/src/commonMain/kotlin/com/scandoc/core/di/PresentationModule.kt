package com.scandoc.core.di

import com.scandoc.presentation.camera.CameraViewModel
import com.scandoc.presentation.crop.CropViewModel
import com.scandoc.presentation.library.LibraryViewModel
import com.scandoc.presentation.navigation.AppViewModel
import com.scandoc.presentation.tools.ToolsViewModel
import com.scandoc.presentation.viewer.ViewerViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { AppViewModel() }
    factory { ToolsViewModel() }
    factory { CameraViewModel(cameraController = get(), captureFrame = get(), detectEdges = get()) }
    factory {
        CropViewModel(
            applyPerspective = get(),
            applyFilter = get(),
            createDocument = get(),
            sessionHolder = get(),
        )
    }
    factory {
        LibraryViewModel(
            observeDocuments = get(),
            searchDocuments = get(),
            deleteDocument = get(),
            renameDocument = get(),
        )
    }
    factory {
        ViewerViewModel(
            getDocument = get(),
            exportPdf = get(),
            runOcrOnPage = get(),
        )
    }
}
