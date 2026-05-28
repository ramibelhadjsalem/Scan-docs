package com.scandoc.core.di

import com.scandoc.presentation.camera.CameraViewModel
import com.scandoc.presentation.crop.CropViewModel
import com.scandoc.presentation.library.LibraryViewModel
import com.scandoc.presentation.viewer.ViewerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { CameraViewModel(captureFrame = get(), detectEdges = get()) }
    viewModel { CropViewModel(applyPerspective = get(), applyFilter = get(), saveDocument = get()) }
    viewModel {
        LibraryViewModel(
            observeDocuments = get(),
            searchDocuments = get(),
            deleteDocument = get(),
        )
    }
    viewModel {
        ViewerViewModel(
            getDocument = get(),
            exportPdf = get(),
        )
    }
}
