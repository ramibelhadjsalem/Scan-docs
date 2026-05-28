package com.scandoc.data.local

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.scandoc.db.ScanDocDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual class DatabaseFactory : KoinComponent {
    actual fun create(): ScanDocDatabase {
        val context: Context = get()
        return ScanDocDatabase(
            driver = AndroidSqliteDriver(
                schema = ScanDocDatabase.Schema,
                context = context,
                name = "scandoc.db",
            ),
        )
    }
}
