package com.scandoc.core.di

import com.scandoc.data.repository.DocumentRepositoryImpl
import com.scandoc.data.repository.ImageRepositoryImpl
import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.repository.ImageRepository
import org.koin.dsl.module

val dataModule = module {
    single<DocumentRepository> { DocumentRepositoryImpl(database = get()) }
    single<ImageRepository> { ImageRepositoryImpl(fileSystem = get()) }
}
