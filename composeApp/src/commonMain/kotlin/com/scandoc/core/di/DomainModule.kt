package com.scandoc.core.di

import com.scandoc.domain.usecase.camera.CaptureFrameUseCase
import com.scandoc.domain.usecase.camera.DetectEdgesUseCase
import com.scandoc.domain.usecase.crop.ApplyFilterUseCase
import com.scandoc.domain.usecase.crop.ApplyPerspectiveUseCase
import com.scandoc.domain.usecase.export.ExportImagesUseCase
import com.scandoc.domain.usecase.export.ExportPdfUseCase
import com.scandoc.domain.usecase.library.DeleteDocumentUseCase
import com.scandoc.domain.usecase.library.ObserveDocumentsUseCase
import com.scandoc.domain.usecase.library.SearchDocumentsUseCase
import com.scandoc.domain.usecase.ocr.RunOcrUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { ObserveDocumentsUseCase(get()) }
    factory { SearchDocumentsUseCase(get()) }
    factory { DeleteDocumentUseCase(get(), get()) }
    factory { CaptureFrameUseCase(get()) }
    factory { DetectEdgesUseCase(get()) }
    factory { ApplyPerspectiveUseCase(get()) }
    factory { ApplyFilterUseCase(get()) }
    factory { ExportPdfUseCase(get(), get()) }
    factory { ExportImagesUseCase(get()) }
    factory { RunOcrUseCase(get()) }
}
