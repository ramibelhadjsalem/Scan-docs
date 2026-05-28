# ScanDoc — Testing Guide

## Philosophy
- Write the test BEFORE the implementation (test-first for all use cases)
- Tests document expected behavior — they are the living specification
- Prefer fakes over mocks — fakes are simpler, faster, and catch more bugs
- Tests must be deterministic — no sleep, no real time, no random data

## Framework
- `kotlin.test` for assertions (multiplatform)
- `kotlinx.coroutines.test` for coroutine testing (`runTest`, `TestCoroutineScheduler`)
- `app.cash.turbine` for Flow testing
- No Mockito, MockK, or other mocking frameworks — use fakes

## Test Location
```
composeApp/src/
├── commonTest/kotlin/com/scandoc/
│   ├── domain/
│   │   └── usecase/          ← use case unit tests
│   └── data/
│       └── repository/       ← repository unit tests (with fake DB)
└── androidTest/              ← Android integration tests (UI, camera)
```

## Naming Convention
```kotlin
// Pattern: should <expected result> when <condition>
@Test
fun `should return documents ordered by date when library loads`() { ... }

@Test
fun `should emit error outcome when ocr engine fails`() { ... }

@Test
fun `should filter documents by query when search intent received`() { ... }
```

## Fake Pattern
Create fakes in `commonTest/kotlin/com/scandoc/fake/`:

```kotlin
class FakeDocumentRepository : DocumentRepository {
    val documents = mutableListOf<Document>()
    var shouldFail = false

    override fun observeAll(): Flow<List<Document>> =
        flowOf(documents.toList())

    override suspend fun save(document: Document): Outcome<Unit> =
        if (shouldFail) Outcome.Failure(RuntimeException("fake error"))
        else Outcome.Success(Unit).also { documents.add(document) }

    override suspend fun delete(id: String): Outcome<Unit> {
        documents.removeAll { it.id == id }
        return Outcome.Success(Unit)
    }

    override suspend fun getById(id: String): Document? =
        documents.find { it.id == id }

    override fun search(query: String): Flow<List<Document>> =
        flowOf(documents.filter { it.name.contains(query, ignoreCase = true) })
}
```

## Use Case Test Template

```kotlin
class RunOcrUseCaseTest {

    private val fakeOcrEngine = FakeOcrEngine()
    private val useCase = RunOcrUseCase(fakeOcrEngine)

    @Test
    fun `should return ocr result when engine succeeds`() = runTest {
        val imageBytes = ByteArray(100)
        fakeOcrEngine.resultToReturn = OcrResult(
            fullText = "Hello World",
            blocks = emptyList(),
            confidence = 0.95f,
            language = "en",
        )

        val result = useCase(imageBytes)

        assertIs<Outcome.Success<OcrResult>>(result)
        assertEquals("Hello World", result.value.fullText)
    }

    @Test
    fun `should return failure outcome when engine throws`() = runTest {
        val imageBytes = ByteArray(100)
        fakeOcrEngine.shouldThrow = true

        val result = useCase(imageBytes)

        assertIs<Outcome.Failure>(result)
    }
}
```

## Flow Testing with Turbine

```kotlin
@Test
fun `should emit updated list when document is deleted`() = runTest {
    val repo = FakeDocumentRepository()
    repo.documents.add(testDocument())
    val useCase = ObserveDocumentsUseCase(repo)

    useCase().test {
        val initial = awaitItem()
        assertEquals(1, initial.size)

        repo.documents.clear()
        // trigger reemission in fake...

        val updated = awaitItem()
        assertEquals(0, updated.size)

        cancelAndIgnoreRemainingEvents()
    }
}
```

## ViewModel Testing

```kotlin
@Test
fun `should update state with documents when load intent dispatched`() = runTest {
    val repo = FakeDocumentRepository()
    repo.documents.add(testDocument(name = "Invoice"))
    val vm = LibraryViewModel(
        observeDocuments = ObserveDocumentsUseCase(repo),
        searchDocuments = SearchDocumentsUseCase(repo),
    )

    vm.state.test {
        val initial = awaitItem()
        assertTrue(initial.isLoading)

        val loaded = awaitItem()
        assertEquals(1, loaded.documents.size)
        assertEquals("Invoice", loaded.documents.first().name)

        cancelAndIgnoreRemainingEvents()
    }
}
```

## Test Data Helpers
Create `commonTest/kotlin/com/scandoc/TestFixtures.kt`:

```kotlin
fun testDocument(
    id: String = "doc-001",
    name: String = "Test Document",
    pages: List<Page> = emptyList(),
): Document = Document(
    id = id,
    name = name,
    createdAt = Instant.fromEpochMilliseconds(0),
    updatedAt = Instant.fromEpochMilliseconds(0),
    pages = pages,
    thumbnailPath = null,
    tags = emptyList(),
)

fun testPage(
    id: String = "page-001",
    orderIndex: Int = 0,
): Page = Page(
    id = id,
    orderIndex = orderIndex,
    imagePath = "/fake/path/page.jpg",
    ocrResult = null,
    width = 1080,
    height = 1920,
)
```

## Coverage Goals
- Domain layer (use cases + models): **≥ 80%**
- Data layer (repositories + mappers): **≥ 70%**
- Presentation layer (ViewModels): **≥ 60%**
- Platform bridges: not unit-tested (integration/manual)
