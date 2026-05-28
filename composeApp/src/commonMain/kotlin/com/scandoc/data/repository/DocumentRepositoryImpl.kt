package com.scandoc.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.scandoc.data.local.mapper.toDomain
import com.scandoc.data.local.mapper.toStorageTags
import com.scandoc.db.ScanDocDatabase
import com.scandoc.domain.model.Document
import com.scandoc.domain.repository.DocumentRepository
import com.scandoc.domain.result.Outcome
import com.scandoc.domain.result.toOutcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class DocumentRepositoryImpl(
    private val database: ScanDocDatabase,
) : DocumentRepository {

    private val queries = database.scanDocDatabaseQueries

    override fun observeAll(): Flow<List<Document>> =
        queries.selectAllDocuments()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map { it.toDomain(pages = pagesForDocument(it.id)) } }

    override suspend fun getById(id: String): Document? =
        queries.selectDocumentById(id)
            .executeAsOneOrNull()
            ?.toDomain(pages = pagesForDocument(id))

    override fun search(query: String): Flow<List<Document>> {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) return observeAll()

        val ocrMatches = queries.searchByOcrText(trimmedQuery)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows -> rows.map { it.toDomain(pages = pagesForDocument(it.id)) } }

        val nameMatches = observeAll()
            .map { documents ->
                documents.filter { it.name.contains(trimmedQuery, ignoreCase = true) }
            }

        return combine(ocrMatches, nameMatches) { ocrDocuments, namedDocuments ->
            (ocrDocuments + namedDocuments)
                .distinctBy { it.id }
                .sortedByDescending { it.updatedAt }
        }
    }

    override suspend fun save(document: Document): Outcome<Unit> =
        runCatching {
            database.transaction {
                val pageCount = document.pages.size.toLong()
                val tags = document.tags.toStorageTags()
                val existing = queries.selectDocumentById(document.id).executeAsOneOrNull()

                if (existing == null) {
                    queries.insertDocument(
                        id = document.id,
                        name = document.name,
                        createdAt = document.createdAt.toEpochMilliseconds(),
                        updatedAt = document.updatedAt.toEpochMilliseconds(),
                        pageCount = pageCount,
                        thumbnailPath = document.thumbnailPath,
                        tags = tags,
                    )
                } else {
                    queries.updateDocument(
                        name = document.name,
                        updatedAt = document.updatedAt.toEpochMilliseconds(),
                        pageCount = pageCount,
                        thumbnailPath = document.thumbnailPath,
                        tags = tags,
                        id = document.id,
                    )
                }

                queries.deletePagesByDocument(document.id)
                document.pages.forEach { page ->
                    queries.insertPage(
                        id = page.id,
                        documentId = document.id,
                        orderIndex = page.orderIndex.toLong(),
                        imagePath = page.imagePath,
                        ocrText = page.ocrResult?.fullText,
                        ocrConfidence = page.ocrResult?.confidence?.toDouble(),
                        width = page.width.toLong(),
                        height = page.height.toLong(),
                    )
                }
            }
        }.toOutcome()

    override suspend fun delete(id: String): Outcome<Unit> =
        runCatching { queries.deleteDocument(id) }.toOutcome()

    private fun pagesForDocument(documentId: String) =
        queries.selectPagesForDocument(documentId).executeAsList()
}
