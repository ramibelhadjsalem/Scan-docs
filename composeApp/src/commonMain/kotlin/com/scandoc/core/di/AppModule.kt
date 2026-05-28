package com.scandoc.core.di

import org.koin.dsl.module

val appModule = module {
    includes(dataModule, domainModule, presentationModule, platformModule)
}
