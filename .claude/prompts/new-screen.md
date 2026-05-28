# Template: Creating a New Screen

Use this template when adding a new screen to ScanDoc.

## Checklist
- [ ] State file created (`<Screen>State.kt` with State + Intent + Effect)
- [ ] ViewModel created (`<Screen>ViewModel.kt`)
- [ ] Screen composable created (`<Screen>Screen.kt`) — stateless
- [ ] Route composable created (connects ViewModel to Screen)
- [ ] Navigation wired in `RootComponent` and `RootContent`
- [ ] Koin binding added to `PresentationModule.kt`
- [ ] Unit test for ViewModel created
- [ ] `@Preview` added to Screen composable

## File Locations
```
presentation/<screen-name>/
├── <Screen>State.kt          ← State + Intent + Effect (one file)
├── <Screen>ViewModel.kt
├── <Screen>Screen.kt         ← stateless composable + Route composable
└── component/
    └── ...                   ← sub-composables for this screen
```

## State File Template

```kotlin
package com.scandoc.presentation.<screen>

data class <Screen>State(
    val isLoading: Boolean = false,
    val error: String? = null,
    // screen-specific fields...
)

sealed interface <Screen>Intent {
    // user actions...
}

sealed interface <Screen>Effect {
    // one-shot events (navigation, toasts)...
}
```

## ViewModel Template

```kotlin
package com.scandoc.presentation.<screen>

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class <Screen>ViewModel(
    // inject use cases here
) : ViewModel() {

    private val _state = MutableStateFlow(<Screen>State())
    val state: StateFlow<<Screen>State> = _state.asStateFlow()

    private val _effects = Channel<<Screen>Effect>(Channel.BUFFERED)
    val effects: Flow<<Screen>Effect> = _effects.receiveAsFlow()

    init {
        onIntent(<Screen>Intent.Load)
    }

    fun onIntent(intent: <Screen>Intent) {
        when (intent) {
            // handle each intent
        }
    }
}
```

## Screen Composable Template

```kotlin
package com.scandoc.presentation.<screen>

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

// Route — connects ViewModel
@Composable
fun <Screen>Route(
    onNavigateTo...: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<<Screen>ViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                // handle effects
            }
        }
    }

    <Screen>Screen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

// Screen — pure UI
@Composable
fun <Screen>Screen(
    state: <Screen>State,
    onIntent: (<Screen>Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // UI implementation
}

@Preview
@Composable
private fun <Screen>ScreenPreview() {
    ScanDocTheme {
        <Screen>Screen(
            state = <Screen>State(),
            onIntent = {},
        )
    }
}
```
