package com.scandoc.core.di

import com.scandoc.presentation.camera.CameraViewModel
import com.scandoc.presentation.crop.CropViewModel
import com.scandoc.presentation.library.LibraryViewModel
import com.scandoc.presentation.viewer.ViewerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LibraryViewModel(get(), get(), get()) }
    viewModel { CameraViewModel(get(), get()) }
    viewModel { CropViewModel(get(), get()) }
    viewModel { ViewerViewModel(get()) }
}
