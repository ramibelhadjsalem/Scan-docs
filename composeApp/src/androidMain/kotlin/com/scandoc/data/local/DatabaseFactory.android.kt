package com.scandoc.data.local

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.scandoc.db.ScanDocDatabase

// Android actual — AndroidSqliteDriver (Phase 4)
actual class DatabaseFactory(
    private val context: Context,
) {
    actual fun create(): ScanDocDatabase =
        ScanDocDatabase(
            AndroidSqliteDriver(
                schema = ScanDocDatabase.Schema,
                context = context,
                name = "scandoc.db",
            ),
        )
}
