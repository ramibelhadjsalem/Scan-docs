# Template: Creating a New Repository

Use this template when adding a new repository to ScanDoc.

## Rules (from architecture.md)
- Interface lives in `domain/repository/` — zero framework imports
- Implementation lives in `data/repository/`
- DB types (SQLDelight entities) never cross the repository boundary
- Mappers live in `data/local/mapper/`
- Use SQLDelight's `asFlow()` for observable queries
- Return `Outcome<T>` from suspend functions that can fail

## Checklist
- [ ] Interface created in `domain/repository/<Noun>Repository.kt`
- [ ] Mapper created in `data/local/mapper/<Noun>Mapper.kt`
- [ ] Fake created in `commonTest/.../fake/Fake<Noun>Repository.kt`
- [ ] Implementation created in `data/repository/<Noun>RepositoryImpl.kt`
- [ ] Koin binding added to `DataModule.kt`
- [ ] Repository impl unit test written against fake DB

## File Locations
```
domain/repository/
└── <Noun>Repository.kt                ← interface

data/repository/
└── <Noun>RepositoryImpl.kt            ← implementation

data/local/mapper/
└── <Noun>Mapper.kt                    ← DB row ↔ domain model

commonTest/.../fake/
└── Fake<Noun>Repository.kt            ← test fake
```

## Interface Template

```kotlin
package com.scandoc.domain.repository

import com.scandoc.core.result.Outcome
import com.scandoc.domain.model.<Noun>
import kotlinx.coroutines.flow.Flow

interface <Noun>Repository {
    fun observeAll(): Flow<List<<Noun>>>
    suspend fun getById(id: String): <Noun>?
    suspend fun save(item: <Noun>): Outcome<Unit>
    suspend fun delete(id: String): Outcome<Unit>
}
```

## Mapper Template

```kotlin
package com.scandoc.data.local.mapper

import com.scandoc.db.<Noun>Entity
import com.scandoc.domain.model.<Noun>

fun <Noun>Entity.toDomain(): <Noun> = <Noun>(
    id = id,
    // map fields...
)

fun <Noun>.toEntity(): <Noun>Entity = <Noun>Entity(
    id = id,
    // map fields...
)
```

## Implementation Template

```kotlin
package com.scandoc.data.repository

import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome
import com.scandoc.data.local.mapper.toDomain
import com.scandoc.data.local.mapper.toEntity
import com.scandoc.domain.model.<Noun>
import com.scandoc.domain.repository.<Noun>Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class <Noun>RepositoryImpl(
    private val db: ScanDocDatabase,
) : <Noun>Repository {

    override fun observeAll(): Flow<List<<Noun>>> =
        db.<noun>Queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: String): <Noun>? =
        db.<noun>Queries.selectById(id).executeAsOneOrNull()?.toDomain()

    override suspend fun save(item: <Noun>): Outcome<Unit> =
        runCatching {
            db.<noun>Queries.insert(item.toEntity())
        }.toOutcome()

    override suspend fun delete(id: String): Outcome<Unit> =
        runCatching {
            db.<noun>Queries.deleteById(id)
        }.toOutcome()
}
```

## Fake Template (for tests)

```kotlin
package com.scandoc.fake

import com.scandoc.core.result.Outcome
import com.scandoc.domain.model.<Noun>
import com.scandoc.domain.repository.<Noun>Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class Fake<Noun>Repository : <Noun>Repository {

    private val store = MutableStateFlow<List<<Noun>>>(emptyList())
    var shouldFail = false

    override fun observeAll(): Flow<List<<Noun>>> = store

    override suspend fun getById(id: String): <Noun>? =
        store.value.find { it.id == id }

    override suspend fun save(item: <Noun>): Outcome<Unit> {
        if (shouldFail) return Outcome.Failure(RuntimeException("fake failure"))
        store.value = store.value.filterNot { it.id == item.id } + item
        return Outcome.Success(Unit)
    }

    override suspend fun delete(id: String): Outcome<Unit> {
        if (shouldFail) return Outcome.Failure(RuntimeException("fake failure"))
        store.value = store.value.filterNot { it.id == id }
        return Outcome.Success(Unit)
    }
}
```

## Koin Registration
In `core/di/DataModule.kt`:

```kotlin
single<<Noun>Repository> { <Noun>RepositoryImpl(get()) }
```
