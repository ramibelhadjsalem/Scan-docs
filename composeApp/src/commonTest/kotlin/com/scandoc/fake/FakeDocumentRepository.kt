package com.scandoc.fake

import com.scandoc.domain.result.Outcome
import com.scandoc.domain.model.Document
import com.scandoc.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeDocumentRepository : DocumentRepository {

    private val store = MutableStateFlow<List<Document>>(emptyList())
    var shouldFail = false

    fun seed(vararg documents: Document) {
        store.value = documents.toList()
    }

    override fun observeAll(): Flow<List<Document>> = store

    override suspend fun getById(id: String): Document? =
        store.value.find { it.id == id }

    override fun search(query: String): Flow<List<Document>> =
        store.map { docs ->
            docs.filter { doc ->
                doc.name.contains(query, ignoreCase = true) ||
                    doc.pages.any { page ->
                        page.ocrResult?.fullText?.contains(query, ignoreCase = true) == true
                    }
            }
        }

    override suspend fun save(document: Document): Outcome<Unit> {
        if (shouldFail) return Outcome.Failure(RuntimeException("fake failure"))
        store.value = store.value.filterNot { it.id == document.id } + document
        return Outcome.Success(Unit)
    }

    override suspend fun delete(id: String): Outcome<Unit> {
        if (shouldFail) return Outcome.Failure(RuntimeException("fake failure"))
        store.value = store.value.filterNot { it.id == id }
        return Outcome.Success(Unit)
    }
}
