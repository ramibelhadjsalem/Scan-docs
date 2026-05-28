# ScanDoc — Clean Architecture Rules

## Layer Diagram

```
┌─────────────────────────────────────────────┐
│              Presentation Layer              │
│   Screens · ViewModels · State · Compose     │
├─────────────────────────────────────────────┤
│               Domain Layer                   │
│   Use Cases · Repository Interfaces · Models │
│         (ZERO framework dependencies)        │
├─────────────────────────────────────────────┤
│                Data Layer                    │
│    Repository Impls · SQLDelight · Okio      │
├─────────────────────────────────────────────┤
│             Platform Bridge                  │
│   expect/actual: Camera · OCR · PDF · FS     │
└─────────────────────────────────────────────┘
```

## Dependency Rule
Dependencies point inward only. Outer layers know about inner layers; inner layers know nothing about outer layers.

```
Presentation → Domain ← Data
                 ↑
              Platform (via expect/actual injected through DI)
```

## Domain Layer
- **Zero** imports from Android, iOS, Compose, SQLDelight, or any third-party library
- Only `kotlinx.coroutines`, `kotlinx.datetime`, `kotlinx.serialization` permitted
- Models are `data class` or `sealed class/interface` — always immutable (`val` only)
- Repository interfaces define contracts; domain never knows the implementation
- Use cases are single-responsibility: one public `invoke` operator function
- Use cases throw no exceptions — they return `Outcome<T>`

## Data Layer
- Implements domain repository interfaces
- May use SQLDelight, Okio, and kotlinx libraries
- Mappers live in `data/local/mapper/` — they translate DB rows ↔ domain models
- `DatabaseFactory` is an `expect` class — drivers differ per platform
- Never leak DB types (e.g., `DocumentEntity`) past the repository boundary

## Presentation Layer
- MVI pattern: every screen has `State`, `Intent`, `Effect`
- `State` — a single `data class`, holds all UI-renderable data
- `Intent` — `sealed interface`, represents user actions
- `Effect` — `sealed interface`, one-shot events (navigation, toasts)
- `ViewModel` holds `StateFlow<State>` and `Flow<Effect>` (via `Channel`)
- Composables are **stateless** — they receive `state` and `onIntent` lambda
- No business logic inside `@Composable` functions

## Platform Bridge
- `expect` declarations live in `commonMain/core/platform/`
- `actual` implementations live in `androidMain/` and `iosMain/`
- `expect` classes may only reference types from `domain/` or `kotlinx.*`
- Every `expect`/`actual` pair must have KDoc explaining platform differences

## MVI Data Flow

```
User Action
    │
    ▼
Intent (sealed interface)
    │
    ▼
ViewModel.onIntent(intent)
    │
    ├──► update State  ──► StateFlow ──► Composable re-renders
    │
    └──► emit Effect   ──► Channel  ──► Composable collects once
                                        (navigation, snackbar, etc.)
```

## Naming Conventions

| Type              | Pattern                  | Example                     |
|-------------------|--------------------------|-----------------------------|
| Domain model      | Noun                     | `Document`, `Page`          |
| Use case          | VerbNounUseCase          | `RunOcrUseCase`             |
| Repository (iface)| NounRepository           | `DocumentRepository`        |
| Repository (impl) | NounRepositoryImpl       | `DocumentRepositoryImpl`    |
| ViewModel         | ScreenViewModel          | `LibraryViewModel`          |
| State file        | ScreenState              | `LibraryState` (+ Intent + Effect) |
| Screen composable | ScreenScreen             | `LibraryScreen`             |
| expect class      | ClassName                | `CameraController`          |
| actual (android)  | ClassName.android.kt     | `CameraController.android.kt` |
| actual (ios)      | ClassName.ios.kt         | `CameraController.ios.kt`   |

## Module Structure

```
composeApp/
└── src/
    ├── commonMain/kotlin/com/scandoc/
    │   ├── core/         ← shared utilities, DI, expect declarations
    │   ├── domain/       ← models, repository interfaces, use cases
    │   ├── data/         ← repository implementations, DB, storage
    │   └── presentation/ ← screens, viewmodels, theme, components
    ├── commonTest/       ← unit tests (domain + data)
    ├── androidMain/      ← Android actuals + entry point
    └── iosMain/          ← iOS actuals
```

## Anti-Patterns (forbidden)
- `import android.*` in commonMain
- `import platform.UIKit.*` in commonMain
- `var` in state data classes
- `!!` anywhere
- `GlobalScope.launch`
- Catching bare `Exception` or `Throwable`
- Business logic in `@Composable` functions
- Direct DB access outside of repository implementations
- Hard-coded strings — use string resources
