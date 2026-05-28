package com.scandoc.core.di

import com.scandoc.data.local.DatabaseFactory
import com.scandoc.data.repository.DocumentRepositoryImpl
import com.scandoc.data.repository.ImageRepositoryImpl
import com.scandoc.data.storage.FileStorage
import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.repository.ImageRepository
import org.koin.dsl.module

val dataModule = module {
    single { DatabaseFactory().create() }
    single { FileStorage(get()) }
    single<DocumentRepository> { DocumentRepositoryImpl(get()) }
    single<ImageRepository> { ImageRepositoryImpl(get()) }
}
