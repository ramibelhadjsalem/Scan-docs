package com.scandoc.core.di

import com.scandoc.core.platform.AppFileSystem
import com.scandoc.core.platform.CameraController
import com.scandoc.core.platform.ImageProcessor
import com.scandoc.core.platform.OcrEngine
import com.scandoc.core.platform.PdfExporter
import com.scandoc.core.platform.PlatformCameraController
import com.scandoc.core.platform.PlatformImageProcessor
import com.scandoc.core.platform.PlatformOcrEngine
import org.koin.dsl.module

val platformModule = module {
    single { AppFileSystem() }
    single { PdfExporter() }
    single<CameraController> { PlatformCameraController() }
    single<ImageProcessor> { PlatformImageProcessor() }
    single<OcrEngine> { PlatformOcrEngine() }
}
