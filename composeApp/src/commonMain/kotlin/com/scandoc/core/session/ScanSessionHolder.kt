package com.scandoc.core.session

class ScanSessionHolder {
    private val pendingPages = mutableListOf<ByteArray>()

    val pageCount: Int get() = pendingPages.size

    fun addPage(bytes: ByteArray) {
        pendingPages.add(bytes)
    }

    fun drainPages(): List<ByteArray> {
        val result = pendingPages.toList()
        pendingPages.clear()
        return result
    }

    fun clear() {
        pendingPages.clear()
    }
}
