package com.scandoc.data.local

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.scandoc.db.ScanDocDatabase

actual class DatabaseFactory(
    private val context: Context,
) {
    actual fun create(): ScanDocDatabase =
        ScanDocDatabase(
            AndroidSqliteDriver(
                schema = ScanDocDatabase.Schema,
                context = context,
                name = "scandoc.db",
                callback = object : AndroidSqliteDriver.Callback(ScanDocDatabase.Schema) {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        db.execSQL("PRAGMA foreign_keys = ON")
                    }
                },
            ),
        )
}
