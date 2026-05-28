# Phase 4/5 Foundation Design

## Goal

Finish ScanDoc Phase 4 and Phase 5 as a strict foundation pass. Phase 4 makes the data layer real enough for library/search/delete flows. Phase 5 wires domain use cases through dependency injection while correcting known architecture drift.

## Scope

This phase includes:

- Restore a usable Gradle wrapper so tests and build checks can run from the repository root.
- Move result handling into the domain layer so domain code does not import `core`.
- Introduce domain-owned platform contracts for camera, image processing, OCR, PDF export, and file paths.
- Update common platform bridge declarations to implement those domain contracts at the platform boundary.
- Implement SQLDelight document/page mapping.
- Implement `DocumentRepositoryImpl` using SQLDelight queries.
- Implement `ImageRepositoryImpl` through a small `FileStorage` abstraction backed by `AppFileSystem`.
- Populate `DataModule.kt` and `DomainModule.kt`.
- Add or update tests for the changed repository/use case behavior.

This phase does not include real CameraX, ML Kit, Vision, PDFKit, or bitmap processing implementations. Those remain Phase 6 platform work.

## Architecture

Domain becomes self-contained. Files under `domain/*` may import only `domain.*`, `kotlin.*`, and approved `kotlinx.*` APIs. `Outcome` moves from `core.result` to `domain.result`, because repository interfaces and use cases are domain contracts.

Use cases that need camera, OCR, image, PDF, or paths depend on interfaces in `domain.platform`. The existing `core.platform` expect classes remain the platform boundary and implement those interfaces. This keeps `expect/actual` out of domain while still allowing platform implementations to be injected later.

Data depends inward on domain. `DocumentRepositoryImpl` owns database access and maps SQLDelight rows to immutable domain models. `ImageRepositoryImpl` owns image file persistence through `FileStorage`; it returns saved paths and byte payloads wrapped in `Outcome`.

## Data Behavior

`DocumentRepositoryImpl.observeAll()` emits documents ordered by `updatedAt` descending, with pages loaded for each document in `orderIndex` order.

`getById(id)` returns a full `Document` with pages, or `null` when absent.

`search(query)` returns all documents for a blank query. For non-blank queries, it searches OCR text through SQLDelight and supplements that with document-name matching in repository code, because the existing SQLDelight query only covers page OCR text. Results remain ordered by `updatedAt` descending.

`save(document)` writes the document row and replaces its page rows in a single logical save operation. `delete(id)` removes the document and its pages.

Tags continue to be stored as a comma-separated string for now, matching the existing schema. Tag values containing commas are not supported in this phase.

## DI Behavior

`DataModule.kt` registers:

- SQLDelight database creation through `DatabaseFactory`.
- `FileStorage`.
- `DocumentRepository`.
- `ImageRepository`.

`DomainModule.kt` registers all existing use cases:

- Camera: `CaptureFrameUseCase`, `DetectEdgesUseCase`.
- Crop: `ApplyPerspectiveUseCase`, `ApplyFilterUseCase`.
- OCR: `RunOcrUseCase`.
- Export: `ExportPdfUseCase`, `ExportImagesUseCase`.
- Library: `ObserveDocumentsUseCase`, `SearchDocumentsUseCase`, `DeleteDocumentUseCase`.

Phase 6 will populate `PlatformModule.kt` with real platform actuals. Phase 4/5 may adjust constructor types so those future bindings compile cleanly, but it will not add fake runtime platform bindings to production DI.

## Error Handling

Operations that can fail return `Outcome<T>`. Use cases do not throw intentionally. Repository and storage implementations wrap SQLDelight or filesystem failures in `Outcome.Failure`.

No generic `catch (Exception)` is introduced. Where failure wrapping is needed, the implementation uses `runCatching { ... }.toOutcome()` or catches specific platform/data exceptions only when the code needs special handling.

## Testing

Use case behavior remains test-first. Existing tests are updated to import `domain.result.Outcome` and domain platform contracts.

Repository tests focus on pure mapping and repository-observable behavior that can run in `commonTest` without adding a new dependency. SQLDelight driver-backed integration tests are deferred unless they can run with existing dependencies.

Verification target:

- `./gradlew :composeApp:allTests`
- `./gradlew :composeApp:compileKotlinMetadata`

If platform SDK tooling blocks a target, record the exact failing command and error.
