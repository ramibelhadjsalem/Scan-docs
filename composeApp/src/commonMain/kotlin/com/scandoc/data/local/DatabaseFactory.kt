package com.scandoc.data.local

import com.scandoc.db.ScanDocDatabase

/**
 * Creates the platform-specific SQLDelight database driver.
 * Android: AndroidSqliteDriver with Context.
 * iOS: NativeSqliteDriver.
 */
expect class DatabaseFactory() {
    fun create(): ScanDocDatabase
}
