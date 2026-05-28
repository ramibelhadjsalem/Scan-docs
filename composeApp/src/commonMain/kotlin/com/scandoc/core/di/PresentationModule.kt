package com.scandoc.core.di

import com.scandoc.presentation.camera.CameraViewModel
import com.scandoc.presentation.crop.CropViewModel
import com.scandoc.presentation.library.LibraryViewModel
import com.scandoc.presentation.viewer.ViewerViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory { CameraViewModel(captureFrame = get(), detectEdges = get()) }
    factory { CropViewModel(applyPerspective = get(), applyFilter = get()) }
    factory {
        LibraryViewModel(
            observeDocuments = get(),
            searchDocuments = get(),
            deleteDocument = get(),
        )
    }
    factory {
        ViewerViewModel(
            getDocument = get(),
            exportPdf = get(),
        )
    }
}
