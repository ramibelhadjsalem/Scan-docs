package com.scandoc.core.di

import com.scandoc.core.platform.AppFileSystem
import com.scandoc.core.platform.PdfExporter
import com.scandoc.core.platform.PlatformCameraController
import com.scandoc.core.platform.PlatformImageProcessor
import com.scandoc.core.platform.PlatformOcrEngine
import com.scandoc.data.local.DatabaseFactory
import com.scandoc.db.ScanDocDatabase
import com.scandoc.domain.platform.CameraController
import com.scandoc.domain.platform.ImageProcessor
import com.scandoc.domain.platform.OcrEngine
import org.koin.core.module.Module
import org.koin.dsl.module
import com.scandoc.domain.platform.AppFileSystem as DomainAppFileSystem
import com.scandoc.domain.platform.PdfExporter as DomainPdfExporter

actual val platformModule: Module = module {
    single<DatabaseFactory> { DatabaseFactory() }
    single<ScanDocDatabase> { get<DatabaseFactory>().create() }
    single<DomainAppFileSystem> { AppFileSystem() }
    single<CameraController> { PlatformCameraController() }
    single<ImageProcessor> { PlatformImageProcessor() }
    single<OcrEngine> { PlatformOcrEngine() }
    single<DomainPdfExporter> { PdfExporter() }
}
