package com.scandoc.core.di

import com.scandoc.core.platform.AppFileSystem
import com.scandoc.core.platform.CameraController
import com.scandoc.core.platform.FileSystemAccess
import com.scandoc.core.platform.ImageProcessor
import com.scandoc.core.platform.OcrEngine
import com.scandoc.core.platform.PdfExportAccess
import com.scandoc.core.platform.PdfExporter
import com.scandoc.core.platform.PlatformCameraController
import com.scandoc.core.platform.PlatformImageProcessor
import com.scandoc.core.platform.PlatformOcrEngine
import org.koin.dsl.module

val platformModule = module {
    single { AppFileSystem() } bind FileSystemAccess::class
    single { PdfExporter() } bind PdfExportAccess::class
    single<CameraController> { PlatformCameraController() }
    single<ImageProcessor> { PlatformImageProcessor() }
    single<OcrEngine> { PlatformOcrEngine() }
}
