package com.scandoc.domain.repository

import com.scandoc.domain.result.Outcome
import com.scandoc.domain.model.Document
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {
    fun observeAll(): Flow<List<Document>>
    suspend fun getById(id: String): Document?
    fun search(query: String): Flow<List<Document>>
    suspend fun save(document: Document): Outcome<Unit>
    suspend fun delete(id: String): Outcome<Unit>
}
