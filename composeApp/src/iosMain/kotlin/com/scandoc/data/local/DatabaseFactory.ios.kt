package com.scandoc.data.local

import com.scandoc.db.ScanDocDatabase

// iOS actual — NativeSqliteDriver (Phase 4)
actual class DatabaseFactory actual constructor() {
    actual fun create(): ScanDocDatabase {
        error("DatabaseFactory.create() not yet implemented — Phase 4")
    }
}
