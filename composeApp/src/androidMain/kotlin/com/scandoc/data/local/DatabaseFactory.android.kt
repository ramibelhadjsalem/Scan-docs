package com.scandoc.data.local

import com.scandoc.db.ScanDocDatabase

// Android actual — AndroidSqliteDriver (Phase 4)
actual class DatabaseFactory {
    actual fun create(): ScanDocDatabase {
        error("DatabaseFactory.create() not yet implemented — Phase 4")
    }
}
