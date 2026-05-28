package com.scandoc.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome
import com.scandoc.data.local.mapper.toDomain
import com.scandoc.db.ScanDocDatabase
import com.scandoc.domain.model.Document
import com.scandoc.domain.repository.DocumentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DocumentRepositoryImpl(
    private val db: ScanDocDatabase,
) : DocumentRepository {

    override fun observeAll(): Flow<List<Document>> =
        db.scanDocDatabaseQueries.selectAllDocuments()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { entity ->
                    val pages = db.scanDocDatabaseQueries
                        .selectPagesForDocument(entity.id)
                        .executeAsList()
                    entity.toDomain(pages)
                }
            }

    override suspend fun getById(id: String): Document? = withContext(Dispatchers.IO) {
        val entity = db.scanDocDatabaseQueries
            .selectDocumentById(id)
            .executeAsOneOrNull() ?: return@withContext null
        val pages = db.scanDocDatabaseQueries
            .selectPagesForDocument(id)
            .executeAsList()
        entity.toDomain(pages)
    }

    override fun search(query: String): Flow<List<Document>> =
        db.scanDocDatabaseQueries.searchByOcrText(query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { entity ->
                    val pages = db.scanDocDatabaseQueries
                        .selectPagesForDocument(entity.id)
                        .executeAsList()
                    entity.toDomain(pages)
                }
            }

    override suspend fun save(document: Document): Outcome<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            db.transaction {
                val exists = db.scanDocDatabaseQueries
                    .selectDocumentById(document.id)
                    .executeAsOneOrNull() != null
                if (exists) {
                    db.scanDocDatabaseQueries.updateDocument(
                        name = document.name,
                        updatedAt = document.updatedAt.toEpochMilliseconds(),
                        pageCount = document.pages.size.toLong(),
                        thumbnailPath = document.thumbnailPath,
                        tags = document.tags.joinToString(","),
                        id = document.id,
                    )
                } else {
                    db.scanDocDatabaseQueries.insertDocument(
                        id = document.id,
                        name = document.name,
                        createdAt = document.createdAt.toEpochMilliseconds(),
                        updatedAt = document.updatedAt.toEpochMilliseconds(),
                        pageCount = document.pages.size.toLong(),
                        thumbnailPath = document.thumbnailPath,
                        tags = document.tags.joinToString(","),
                    )
                }
                db.scanDocDatabaseQueries.deletePagesByDocument(document.id)
                document.pages.forEach { page ->
                    db.scanDocDatabaseQueries.insertPage(
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
    }

    override suspend fun delete(id: String): Outcome<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            db.scanDocDatabaseQueries.deleteDocument(id)
        }.toOutcome()
    }
}
