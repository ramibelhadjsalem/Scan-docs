# ScanDoc — Working Rules for Codex

## Identity
You are working on ScanDoc — a Kotlin Multiplatform document scanner.
Code lives in `composeApp/src/`. iOS entry is `iosApp/`.

## Hard Rules
- NEVER add a dependency without justifying it
- NEVER write Android-specific code in commonMain
- NEVER use `!!` (non-null assertion) — use `?: error("reason")` or proper null handling
- NEVER catch generic `Exception` — catch specific ones
- NEVER use `GlobalScope` — always scoped coroutines
- ALWAYS write the use case test BEFORE the use case implementation
- ALWAYS use `expect/actual` only at the platform boundary (camera, OCR, PDF, paths)
- ALWAYS use sealed classes/interfaces for finite states
- ALWAYS prefer composition over inheritance

## File Naming
- ViewModels: `<Screen>ViewModel.kt`
- States: `<Screen>State.kt` (in same file as Intent + Effect)
- Use cases: `<Verb><Noun>UseCase.kt` (e.g. `RunOcrUseCase.kt`)
- Repositories interface: `<Noun>Repository.kt` in domain
- Repository impl: `<Noun>RepositoryImpl.kt` in data

## Layer Imports (enforce strictly)
- `domain/*` → may import: kotlinx.* ONLY
- `data/*` → may import: domain.*, kotlinx.*, SQLDelight, Okio
- `presentation/*` → may import: domain.*, kotlinx.*, Compose, ViewModel
- `platform/*` (commonMain expect) → may import: kotlinx.* ONLY
- Platform actuals → may import their platform's APIs

## When You're Unsure
1. Re-read this file
2. Re-read `rules/architecture.md`
3. Ask before deviating


<claude-mem-context>
# Memory Context

# [Scan-docs] recent context, 2026-05-28 3:42am GMT+1

No previous sessions found.
</claude-mem-context>