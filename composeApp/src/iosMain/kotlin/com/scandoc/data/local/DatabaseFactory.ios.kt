package com.scandoc.data.local

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.scandoc.db.ScanDocDatabase

// iOS actual — NativeSqliteDriver (Phase 4)
actual class DatabaseFactory {
    actual fun create(): ScanDocDatabase =
        ScanDocDatabase(
            NativeSqliteDriver(
                schema = ScanDocDatabase.Schema,
                name = "scandoc.db",
            ),
        )
}
