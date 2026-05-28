# ScanDoc — Compose Multiplatform Style Guide

## Core Principle: Stateless Composables
Every composable should be stateless by default. State lives in the ViewModel.

```kotlin
// Good — stateless, testable, previewable
@Composable
fun LibraryScreen(
    state: LibraryState,
    onIntent: (LibraryIntent) -> Unit,
    modifier: Modifier = Modifier,
) { ... }

// Bad — composable owns state
@Composable
fun LibraryScreen() {
    val viewModel = koinViewModel<LibraryViewModel>()  // state captured inside
    ...
}
```

## State Hoisting
- All state that affects rendering lives in `ViewModel` as `StateFlow<State>`
- Effects (navigation, toasts) collected in `LaunchedEffect` at screen entry point
- Never pass `ViewModel` into a composable — pass `state` and `onIntent` lambda

## Naming
- `@Composable` functions: PascalCase (e.g., `DocumentCard`, `FilterStrip`)
- Preview functions: `@Preview fun <Name>Preview()`
- Component files named after the primary composable: `DocumentCard.kt`

## Parameters
- `modifier: Modifier = Modifier` is always the last parameter (or last optional)
- Required parameters come before optional ones
- Prefer slot APIs for flexible layouts: `content: @Composable () -> Unit`
- Group related parameters logically; don't exceed 6–7 params before extracting a data class

## Modifier Usage
- Always pass `modifier` down to the root layout element of the composable
- Never hardcode size/padding inside a component — use `Dimens` constants
- Prefer `Modifier.fillMaxWidth()` over `Modifier.width(360.dp)` for responsive layouts

## Side Effects
- Use `LaunchedEffect(key)` for one-shot effects tied to lifecycle
- Use `DisposableEffect` for cleanup (e.g., camera lifecycle)
- Use `SideEffect` only for non-suspending synchronization with non-Compose code
- Collect `ViewModel.effects` Flow inside `LaunchedEffect(Unit)` at the screen level

## Performance
- Use `remember { }` to memoize expensive calculations
- Use `rememberUpdatedState` for lambdas captured in long-lived effects
- Use `key(id) { }` in `LazyColumn` items — never use index as key for mutable lists
- Avoid creating lambdas inside `@Composable` scope that cause recompositions

## Lists
- Use `LazyColumn` / `LazyRow` for all lists of unknown or large size
- Provide `key =` to every `items { }` call
- Extract item composables into their own files

## Theme Usage
- Access colors via `MaterialTheme.colorScheme.*`
- Access typography via `MaterialTheme.typography.*`
- Access shapes via `MaterialTheme.shapes.*`
- Access custom dimens via `ScanDocDimens.*`
- Never hardcode colors, font sizes, or radii

## Previews
- Every public composable must have a `@Preview`
- Use `ScanDocTheme { }` wrapper in previews
- Provide meaningful preview data (not empty strings / zeros)
- Add `@PreviewLightDark` where dark/light contrast matters

## Anti-Patterns (forbidden)
- Business logic inside `@Composable` — belongs in ViewModel / UseCase
- `remember { mutableStateOf() }` for state that survives screen transitions
- `LocalContext.current` in shared composables (Android-only — breaks iOS)
- Direct DB / repository calls from composables
- `LaunchedEffect` with unstable keys that retrigger constantly
- Nested `LazyColumn` / `LazyRow` without fixed sizes

## Example Screen Structure

```kotlin
// CameraScreen.kt — entry point, connects ViewModel
@Composable
fun CameraRoute(component: CameraComponent) {
    val viewModel = koinViewModel<CameraViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CameraEffect.NavigateToCrop -> component.onCapture(effect.imageBytes)
            }
        }
    }

    CameraScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

// CameraScreen — pure UI
@Composable
fun CameraScreen(
    state: CameraState,
    onIntent: (CameraIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        CameraPreview(modifier = Modifier.fillMaxSize())
        DetectionOverlay(corners = state.detectedCorners)
        ShutterButton(
            onClick = { onIntent(CameraIntent.Capture) },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
```
