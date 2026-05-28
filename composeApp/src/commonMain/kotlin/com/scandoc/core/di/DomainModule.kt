package com.scandoc.core.di

import com.scandoc.domain.usecase.camera.CaptureFrameUseCase
import com.scandoc.domain.usecase.camera.DetectEdgesUseCase
import com.scandoc.domain.usecase.crop.ApplyFilterUseCase
import com.scandoc.domain.usecase.crop.ApplyPerspectiveUseCase
import com.scandoc.domain.usecase.crop.SaveDocumentUseCase
import com.scandoc.domain.usecase.export.ExportImagesUseCase
import com.scandoc.domain.usecase.export.ExportPdfUseCase
import com.scandoc.domain.usecase.library.DeleteDocumentUseCase
import com.scandoc.domain.usecase.library.GetDocumentUseCase
import com.scandoc.domain.usecase.library.ObserveDocumentsUseCase
import com.scandoc.domain.usecase.library.SearchDocumentsUseCase
import com.scandoc.domain.usecase.ocr.RunOcrUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { CaptureFrameUseCase(cameraController = get()) }
    factory { DetectEdgesUseCase(imageProcessor = get()) }
    factory { ApplyPerspectiveUseCase(imageProcessor = get()) }
    factory { ApplyFilterUseCase(imageProcessor = get()) }
    factory { SaveDocumentUseCase(documentRepository = get(), imageRepository = get()) }
    factory { RunOcrUseCase(ocrEngine = get()) }
    factory { ExportPdfUseCase(pdfExporter = get(), fileSystem = get()) }
    factory { ExportImagesUseCase(fileSystem = get()) }
    factory { GetDocumentUseCase(repository = get()) }
    factory { ObserveDocumentsUseCase(repository = get()) }
    factory { SearchDocumentsUseCase(repository = get()) }
    factory { DeleteDocumentUseCase(documentRepository = get(), imageRepository = get()) }
}
