package com.scandoc.data.local

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.scandoc.db.ScanDocDatabase

actual class DatabaseFactory {
    actual fun create(): ScanDocDatabase {
        val driver = NativeSqliteDriver(
            schema = ScanDocDatabase.Schema,
            name = "scandoc.db",
        )
        driver.execute(null, "PRAGMA foreign_keys = ON", 0)
        return ScanDocDatabase(driver)
    }
}
