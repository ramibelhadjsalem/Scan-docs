# ScanDoc — Kotlin Style Guide

## General
- Use trailing commas in multi-line argument lists and parameter lists
- Use named arguments when passing 3 or more parameters
- Prefer `val` over `var` everywhere — mutability is explicit and intentional
- Prefer top-level functions over `object` wrappers unless state is needed
- No `companion object` unless truly needed (factory methods, constants)
- Limit file length to ~150 lines; split if longer

## Null Safety
- NEVER use `!!` — use `?: error("descriptive message")` or safe unwrapping
- Prefer `?.let { }` or `?.also { }` for nullable chains
- Return `null` from functions that can legitimately produce nothing; use `Outcome<T>` when failure needs a reason

## Data Classes
- Use `data class` for all domain models and UI state
- All fields must be `val`
- Provide sensible defaults so partial construction is easy in tests
- Do not add behavior to data classes — put logic in use cases or extension functions

## Sealed Classes / Interfaces
- Use `sealed interface` for `Intent`, `Effect`, and multi-state results
- Use `sealed class` only when subclasses need shared state/properties
- Exhaustive `when` on sealed types — never add an `else` branch on sealed hierarchies

## Coroutines
- Never use `GlobalScope`
- Use `viewModelScope` in ViewModels
- Use `coroutineScope {}` or `supervisorScope {}` for structured concurrency in use cases
- Prefer `Flow` over callbacks for streams of data
- Use `StateFlow` for UI state, `Channel` (BUFFERED) for one-shot effects
- Wrap suspend calls that can fail in `runCatching { }.toOutcome()`

## Functions
- Functions should do one thing — if the name contains "and", split it
- Keep functions under 30 lines; extract private helpers otherwise
- Extension functions live in `core/extension/` or alongside the type they extend
- Suspend functions must be called from a coroutine context — never block

## Collections
- Prefer `List` over `Array` in domain/presentation code
- Use `emptyList()`, `emptyMap()` over `listOf()` / `mapOf()` for empty collections
- Chain collection transformations rather than imperative loops
- Use `associate`, `groupBy`, `partition` to avoid manual map building

## Imports
- No wildcard imports (`import foo.*`)
- Sort imports: stdlib → kotlinx → third-party → project
- Remove unused imports (CI enforces this via detekt)

## Comments
- Write no comments by default
- Only comment when the WHY is non-obvious: hidden constraint, workaround, subtle invariant
- Never describe WHAT the code does — well-named identifiers do that
- KDoc required on all `expect` declarations explaining platform differences

## Formatting
- Max line length: 120 characters
- Indent: 4 spaces (no tabs)
- Opening brace on same line; closing brace on its own line
- Single blank line between top-level declarations
- No blank line after opening brace or before closing brace in short functions

## Examples

```kotlin
// Good — named args, trailing comma, val
data class Document(
    val id: String,
    val name: String,
    val createdAt: Instant,
    val pages: List<Page> = emptyList(),
)

// Good — sealed interface for intent
sealed interface LibraryIntent {
    data object Load : LibraryIntent
    data class Search(val query: String) : LibraryIntent
    data class Delete(val id: String) : LibraryIntent
}

// Good — top-level function, no object wrapper
fun String.toSlug(): String = lowercase().replace(" ", "-")

// Bad — !! usage
val result = maybeNull!!.doSomething()

// Good — safe unwrap with message
val result = maybeNull?.doSomething() ?: error("maybeNull must be non-null at this point")
```
