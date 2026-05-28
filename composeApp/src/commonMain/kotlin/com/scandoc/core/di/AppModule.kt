package com.scandoc.core.di

import com.scandoc.core.session.ScanSessionHolder
import org.koin.dsl.module

val appModule = module {
    single { ScanSessionHolder() }
    includes(dataModule, domainModule, presentationModule, platformModule)
}
