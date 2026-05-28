package com.scandoc.data.local

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.scandoc.db.ScanDocDatabase

actual class DatabaseFactory {
    actual fun create(): ScanDocDatabase =
        ScanDocDatabase(
            driver = NativeSqliteDriver(
                schema = ScanDocDatabase.Schema,
                name = "scandoc.db",
            ),
        )
}
