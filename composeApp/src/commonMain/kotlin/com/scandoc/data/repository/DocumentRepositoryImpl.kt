package com.scandoc.data.repository

import com.scandoc.core.result.Outcome
import com.scandoc.domain.model.Document
import com.scandoc.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DocumentRepositoryImpl : DocumentRepository {
    // Implemented in Phase 4
    override fun observeAll(): Flow<List<Document>> = flowOf(emptyList())
    override suspend fun getById(id: String): Document? = null
    override fun search(query: String): Flow<List<Document>> = flowOf(emptyList())
    override suspend fun save(document: Document): Outcome<Unit> = Outcome.Success(Unit)
    override suspend fun delete(id: String): Outcome<Unit> = Outcome.Success(Unit)
}
