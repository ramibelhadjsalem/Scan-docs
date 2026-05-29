# ScanDoc — UI Overhaul Plan (CamScan-Inspired)
> Both Claude and Codex must follow this plan exactly, phase by phase.
> Each phase is self-contained and independently executable in a fresh context.
> Do NOT start a phase until all preceding phases are complete and compile-clean.

---

## Context

**Project:** Kotlin Multiplatform (Compose Multiplatform) document scanner app.  
**Branch:** `claude/scandoc-kmp-setup-oI1on`  
**Entry point:** `composeApp/src/commonMain/kotlin/com/scandoc/App.kt`  
**Architecture:** MVI — every screen has `State`, `Intent`, `Effect` + `ViewModel`.  
**DI:** Koin (`PresentationModule.kt`, `DomainModule.kt`, etc.)  
**Theme root:** `presentation/theme/` — always use `ScanDocColors.*`, `ScanDocDimens.*`, `MaterialTheme.*`.  

### Hard constraints (from CLAUDE.md)
- NEVER use `!!` — use `?: error("reason")`
- NEVER write Android-specific code in `commonMain`
- NEVER import `android.*` in `commonMain`
- ALWAYS use sealed classes for finite states
- `expect/actual` only at the platform boundary
- No `GlobalScope`

---

## Goal

Transform the app into a CamScan-quality document scanner with:
1. **5-tab bottom navigation** — Home · Files · Camera (FAB) · Tools · Me
2. **Full-screen dark camera** — top controls, mode pill, feature strip, big shutter
3. **Tools screen** — sectioned scrollable grid of tool tiles (stub actions)
4. **Files screen** — existing LibraryScreen with minor polish
5. **Me screen** — simple profile stub

---

## Current State (read before editing)

| File | Role |
|------|------|
| `App.kt` | Root composable, `when(route)` switch, Koin ViewModels |
| `navigation/AppRoute.kt` | Sealed interface with Splash/Library/Camera/Crop/Viewer |
| `navigation/AppViewModel.kt` | Handles back-stack, navigate(), replace(), back() |
| `navigation/AppState.kt` | `data class AppState(route, backStack)` |
| `navigation/RootContent.kt` | **Stub** — body is empty |
| `navigation/RootComponent.kt` | **Stub** — empty interface |
| `presentation/library/LibraryScreen.kt` | Full screen, uses LibraryViewModel |
| `presentation/camera/CameraScreen.kt` | Full screen, uses CameraViewModel |
| `presentation/camera/CameraState.kt` | Has isActive, detectedCorners, isFlashOn, isAutoCapture, captureCountdown, error |
| `presentation/theme/Color.kt` | `ScanDocColors` object |
| `presentation/theme/Dimens.kt` | `ScanDocDimens` object |
| `presentation/theme/Theme.kt` | `ScanDocTheme`, dark Material3 scheme |

---

## Phase 1 — Design Tokens Update

**Goal:** Add new color and dimension tokens needed by all later phases.  
**Files to edit:** exactly 2 files.

### 1-A · `presentation/theme/Color.kt`

Add the following properties to `ScanDocColors` object (keep all existing properties):

```kotlin
// Teal brand — replaces Signal as the primary interactive color
val Teal = Color(0xFF00C896)
val TealDim = Color(0x2600C896)
val TealDeep = Color(0xFF00A87E)

// Navigation bar
val NavSurface = Color(0xFF111315)
val NavIconInactive = Color(0xFF72757E)

// Tools screen (light surface — Tools screen is always light)
val ToolSurface = Color(0xFFF2F4F7)
val ToolCard = Color(0xFFFFFFFF)
val ToolText = Color(0xFF111315)
val ToolTextSub = Color(0xFF72757E)

// Tool icon background tints (pastel circles)
val ToolTintTeal = Color(0xFFDDF5EE)
val ToolTintPurple = Color(0xFFEDE8FA)
val ToolTintBlue = Color(0xFFE3EDFC)
val ToolTintPink = Color(0xFFFDE8EF)
val ToolTintOrange = Color(0xFFFEF0E6)
val ToolTintYellow = Color(0xFFFDF8E2)

// Tool icon foreground tints
val ToolIconTeal = Color(0xFF00A87E)
val ToolIconPurple = Color(0xFF7C5CBF)
val ToolIconBlue = Color(0xFF3A7BD5)
val ToolIconPink = Color(0xFFD95F8A)
val ToolIconOrange = Color(0xFFE07A35)
val ToolIconYellow = Color(0xFFB8960C)
```

### 1-B · `presentation/theme/Dimens.kt`

Add the following properties to `ScanDocDimens` object (keep all existing):

```kotlin
// Bottom navigation
val navBarHeight = 60.dp
val navFabSize = 56.dp
val navFabElevation = 6.dp

// Tool grid
val toolIconContainerSize = 56.dp
val toolIconSize = 28.dp
val toolItemWidth = 76.dp
val toolSectionSpacing = 28.dp

// Camera redesign
val cameraTopBarHeight = 52.dp
val cameraFeatureStripHeight = 40.dp
val shutterSize = 72.dp
val shutterRingWidth = 4.dp
val shutterInnerSize = 58.dp
val modePillHeight = 34.dp
```

### Verification
- `./gradlew :composeApp:compileKotlinAndroid` must succeed with no errors.
- Grep: `ScanDocColors.Teal` and `ScanDocDimens.navBarHeight` must appear in Color.kt and Dimens.kt respectively.

---

## Phase 2 — Navigation Model

**Goal:** Extend `AppRoute` with new top-level destinations, add `NavTab` model, update `AppState` and `AppViewModel` to track selected tab.  
**Files to edit/create:**

### 2-A · `presentation/navigation/AppRoute.kt`

Add these entries to the existing `AppRoute` sealed interface:

```kotlin
data object Home : AppRoute {
    override val path: String = "home"
}

data object Tools : AppRoute {
    override val path: String = "tools"
}

data object Me : AppRoute {
    override val path: String = "me"
}
```

Keep all existing entries (Splash, Library, Camera, Crop, Viewer) unchanged.

### 2-B · New file: `presentation/navigation/NavTab.kt`

```kotlin
package com.scandoc.presentation.navigation

enum class NavTab {
    Home,
    Files,
    Tools,
    Me,
    // Camera is NOT a tab — it is a FAB overlay
}
```

### 2-C · `presentation/navigation/AppState.kt`

Replace the entire file content:

```kotlin
package com.scandoc.presentation.navigation

data class AppState(
    val route: AppRoute = AppRoute.Splash,
    val backStack: List<AppRoute> = listOf(AppRoute.Splash),
    val selectedTab: NavTab = NavTab.Home,
    val isCameraOverlayOpen: Boolean = false,
)

sealed interface AppEffect
```

### 2-D · `presentation/navigation/AppViewModel.kt`

Add the following methods to the existing `AppViewModel` class (do not remove any existing methods):

```kotlin
fun selectTab(tab: NavTab) {
    updateState { it.copy(selectedTab = tab) }
}

fun openCamera() {
    updateState { it.copy(isCameraOverlayOpen = true) }
}

fun closeCamera() {
    updateState { it.copy(isCameraOverlayOpen = false) }
}
```

Also update `onCameraEffect` so that navigating to crop also closes the overlay:

```kotlin
fun onCameraEffect(effect: CameraEffect) {
    when (effect) {
        is CameraEffect.NavigateToCrop -> {
            closeCamera()
            navigate(AppRoute.Crop(effect.imageBytes))
        }
        is CameraEffect.ShowError -> Unit
    }
}
```

### Verification
- `./gradlew :composeApp:compileKotlinAndroid` must succeed.
- Grep: `NavTab` must appear in AppState.kt.
- Grep: `isCameraOverlayOpen` must appear in AppState.kt.

---

## Phase 3 — Bottom Navigation Bar Component

**Goal:** Create a reusable `ScanDocBottomBar` composable with 4 tabs + center Camera FAB gap.  
**File to create:** `presentation/navigation/ScanDocBottomBar.kt`

### Rules
- Uses `NavigationBar` from Material3.
- 4 tab items: Home, Files, Tools, Me — with `NavigationBarItem`.
- Center slot is visually empty (transparent spacer item) — the FAB floats above it from the parent `Box`.
- Active item: icon + label tinted `ScanDocColors.Teal`.
- Inactive: icon + label tinted `ScanDocColors.NavIconInactive`.
- Bar background: `ScanDocColors.NavSurface`.
- Bar height: `ScanDocDimens.navBarHeight` (60dp).
- Labels: 11sp, `FontWeight.Medium`.
- Use Material Icons for icons:
  - Home → `Icons.Outlined.Home` / `Icons.Filled.Home`
  - Files → `Icons.Outlined.FolderOpen` / `Icons.Filled.Folder`
  - Tools → `Icons.Outlined.GridView` / `Icons.Filled.GridView`
  - Me → `Icons.Outlined.Person` / `Icons.Filled.Person`

### Signature

```kotlin
@Composable
fun ScanDocBottomBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    onCameraClick: () -> Unit,
    modifier: Modifier = Modifier,
)
```

### Layout detail

```
NavigationBar (NavSurface bg) {
    NavItem(Home)
    NavItem(Files)
    Spacer(weight 1f)  // hole for the FAB
    NavItem(Tools)
    NavItem(Me)
}
```

The FAB is NOT inside `ScanDocBottomBar`. It is placed by the caller (`App.kt`) in a `Box` that overlaps the bar.

### FAB spec (to be placed by caller, document here for reference)
- Size: 56dp circle
- Background: `ScanDocColors.Teal`
- Icon: `Icons.Filled.CameraAlt`, tint white
- Shadow elevation: 6dp
- Positioned: `Alignment.BottomCenter`, `offset(y = -8.dp)` above the bar

### Verification
- File compiles in isolation.
- Grep: `ScanDocColors.Teal` used for active item tint.
- Grep: `ScanDocColors.NavSurface` used as `containerColor` of `NavigationBar`.

---

## Phase 4 — App.kt: Main Scaffold Wiring

**Goal:** Replace the `when(route)` giant switch in `App.kt` with a `Scaffold` + bottom nav + camera overlay architecture.

**File to edit:** `App.kt`

### New App structure

```
ScanDocTheme {
    Box(fillMaxSize) {
        if (route == Splash) {
            SplashScreen(...)
        } else {
            Scaffold(
                bottomBar = {
                    ScanDocBottomBar(
                        selectedTab = appState.selectedTab,
                        onTabSelected = appViewModel::selectTab,
                        onCameraClick = appViewModel::openCamera,
                    )
                }
            ) { innerPadding ->
                Box(Modifier.padding(innerPadding)) {
                    when (appState.selectedTab) {
                        NavTab.Home -> HomeTabContent(appViewModel)
                        NavTab.Files -> FilesTabContent(appViewModel)
                        NavTab.Tools -> ToolsTabContent()
                        NavTab.Me -> MeScreen()
                    }
                }
            }

            // Camera overlay — full screen, slides over everything
            AnimatedVisibility(
                visible = appState.isCameraOverlayOpen,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                CameraTabContent(appViewModel)
            }

            // Crop overlay (when route is Crop)
            if (route is AppRoute.Crop) {
                CropTabContent(appViewModel, route)
            }

            // Viewer overlay (when route is Viewer)
            if (route is AppRoute.Viewer) {
                ViewerTabContent(appViewModel, route)
            }
        }
    }
}
```

### Tab content private composables (in App.kt, private)

Each is a private `@Composable` that obtains its ViewModel via `koinViewModel()` and wires effects to `appViewModel`:

- `HomeTabContent(appViewModel)` — uses `LibraryViewModel`, shows `LibraryScreen`; `LibraryEffect.NavigateToCamera` now calls `appViewModel.openCamera()` instead of navigating
- `FilesTabContent(appViewModel)` — same as Home for now (same LibraryViewModel, same screen — can differentiate later)
- `ToolsTabContent()` — uses `ToolsViewModel`, shows `ToolsScreen`
- `MeScreen()` — standalone composable, no ViewModel needed yet
- `CameraTabContent(appViewModel)` — uses `CameraViewModel`; on `CameraEffect.NavigateToCrop` calls `appViewModel.onCameraEffect(effect)` which closes overlay + navigates
- `CropTabContent(appViewModel, route)` — uses `CropViewModel`
- `ViewerTabContent(appViewModel, route)` — uses `ViewerViewModel`

### FAB placement

Inside the outer `Box`, position the FAB:

```kotlin
if (route != AppRoute.Splash) {
    FloatingActionButton(
        onClick = appViewModel::openCamera,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = (-38).dp),  // sit above nav bar
        containerColor = ScanDocColors.Teal,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
    ) {
        Icon(Icons.Filled.CameraAlt, contentDescription = "Scan")
    }
}
```

### Verification
- `./gradlew :composeApp:compileKotlinAndroid` must succeed.
- The bottom bar must be visible in every tab except Splash.
- Camera opens as overlay (no navigation, no back stack change for camera itself).

---

## Phase 5 — Camera Screen Redesign

**Goal:** Replace the current prototype camera layout with a full-screen dark camera UI matching the CamScan reference.

**Files to edit/create:**

### 5-A · `presentation/camera/CameraState.kt`

Add to the existing `CameraState` data class:

```kotlin
val selectedMode: CaptureMode = CaptureMode.Single,
val selectedFeature: CameraFeature = CameraFeature.Scanner,
val isHdMode: Boolean = true,
```

Add these enums in the same file:

```kotlin
enum class CaptureMode { Single, Batch }

enum class CameraFeature(val label: String) {
    IdCard("ID Card"),
    Signature("Signature"),
    Scanner("Scanner"),
    ToWord("To Word"),
    BatchScan("Batch"),
}
```

Add intents for new state:

```kotlin
data class SelectMode(val mode: CaptureMode) : CameraIntent
data class SelectFeature(val feature: CameraFeature) : CameraIntent
data object ToggleHd : CameraIntent
```

### 5-B · `presentation/camera/CameraViewModel.kt`

Handle new intents in `onIntent`:

```kotlin
is CameraIntent.SelectMode -> updateState { it.copy(selectedMode = intent.mode) }
is CameraIntent.SelectFeature -> updateState { it.copy(selectedFeature = intent.feature) }
is CameraIntent.ToggleHd -> updateState { it.copy(isHdMode = !it.isHdMode) }
```

### 5-C · `presentation/camera/component/CameraTopBar.kt` (new file)

```kotlin
@Composable
fun CameraTopBar(
    isFlashOn: Boolean,
    isHdMode: Boolean,
    onClose: () -> Unit,
    onToggleFlash: () -> Unit,
    onToggleHd: () -> Unit,
    modifier: Modifier = Modifier,
)
```

Layout: `Row`, `fillMaxWidth`, `WindowInsets.safeDrawing` top padding.
- Left: X close button (32dp tap target, white icon)
- Right cluster: Flash icon, HD badge (`Text("HD", 10sp, teal/white)`), filter icon, overflow `···`
- Background: transparent (camera feed shows through)
- All icons: white, 24dp

### 5-D · `presentation/camera/component/ModeSwitcher.kt` (replace existing)

Pill-shaped toggle between `CaptureMode.Single` ("Simple") and `CaptureMode.Batch` ("Lot").

```kotlin
@Composable
fun ModeSwitcher(
    selected: CaptureMode,
    onSelect: (CaptureMode) -> Unit,
    modifier: Modifier = Modifier,
)
```

Spec:
- Pill container: `ScanDocColors.Ink2.copy(alpha = 0.7f)`, corner radius 20dp, height 34dp
- Selected segment: white background, corner radius 16dp, slight shadow
- Unselected text: `ScanDocColors.Text2`
- Selected text: `ScanDocColors.Ink`, `FontWeight.SemiBold`
- Animated with `animateFloatAsState` on offset

### 5-E · `presentation/camera/component/FeatureStrip.kt` (new file)

Horizontal scrollable list of `CameraFeature` labels.

```kotlin
@Composable
fun FeatureStrip(
    features: List<CameraFeature>,
    selected: CameraFeature,
    onSelect: (CameraFeature) -> Unit,
    modifier: Modifier = Modifier,
)
```

Spec:
- `LazyRow`, `contentPadding = PaddingValues(horizontal = 24.dp)`, `horizontalArrangement = Arrangement.spacedBy(24.dp)`
- Each item: `Text(feature.label)`, 14sp, `FontWeight.Medium`
- Selected: `ScanDocColors.Teal`, + 2dp teal underline bar below
- Unselected: `Color.White.copy(alpha = 0.7f)`
- No background on items, just text + underline indicator
- Height: `ScanDocDimens.cameraFeatureStripHeight` (40dp)

### 5-F · `presentation/camera/component/ShutterButton.kt` (replace existing)

```kotlin
@Composable
fun ShutterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
)
```

Spec:
- Outer ring: 72dp circle, `ScanDocColors.Teal` border 4dp, transparent fill
- Inner circle: 58dp, `Color.White`
- Press animation: `scale = animateFloatAsState(if pressed 0.92f else 1f, spring(stiffness = 500f))`
- Use `Modifier.pointerInput` to detect press/release for scale, `clickable` for action

### 5-G · `presentation/camera/CameraScreen.kt` (replace entire file)

New full-screen layout:

```
Box(fillMaxSize, background = Color.Black) {

    // 1. Camera preview fills everything (existing CameraPreviewView actual)
    CameraPreview(Modifier.fillMaxSize())

    // 2. Detection overlay
    if (state.detectedCorners != null) {
        DetectionCorners(corners = state.detectedCorners, Modifier.fillMaxSize())
    }

    // 3. Top bar — transparent overlay
    CameraTopBar(
        isFlashOn = state.isFlashOn,
        isHdMode = state.isHdMode,
        onClose = { onIntent(CameraIntent.StopCamera) },
        onToggleFlash = { onIntent(CameraIntent.ToggleFlash) },
        onToggleHd = { onIntent(CameraIntent.ToggleHd) },
        modifier = Modifier.align(Alignment.TopCenter),
    )

    // 4. Bottom controls column
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ModeSwitcher(
            selected = state.selectedMode,
            onSelect = { onIntent(CameraIntent.SelectMode(it)) },
        )

        FeatureStrip(
            features = CameraFeature.entries,
            selected = state.selectedFeature,
            onSelect = { onIntent(CameraIntent.SelectFeature(it)) },
        )

        // Shutter row
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Gallery grid icon (stub — 44dp tap target)
            IconButton(onClick = {}) {
                Icon(Icons.Outlined.GridView, tint = Color.White, contentDescription = "Gallery")
            }

            ShutterButton(onClick = { onIntent(CameraIntent.Capture) })

            // Import from files icon (stub)
            IconButton(onClick = {}) {
                Icon(Icons.Outlined.ImageSearch, tint = Color.White, contentDescription = "Import")
            }
        }
    }

    // 5. Error/paused overlay
    if (!state.isActive || state.error != null) {
        // semi-transparent centered badge
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(0.6f), RoundedCornerShape(12.dp))
                .padding(horizontal = 20.dp, vertical = 12.dp),
        ) {
            Text(
                text = state.error ?: "Camera paused",
                color = if (state.error != null) MaterialTheme.colorScheme.error else Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
```

### Verification
- `./gradlew :composeApp:compileKotlinAndroid` must succeed.
- `CaptureMode` and `CameraFeature` enums exist in `CameraState.kt`.
- All 5 components (CameraTopBar, ModeSwitcher, FeatureStrip, ShutterButton, CameraScreen) compile.

---

## Phase 6 — Tools Screen

**Goal:** Create the full Tools screen with sectioned grid of tool tiles.  
**Files to create:**

### 6-A · `presentation/tools/ToolsState.kt`

```kotlin
package com.scandoc.presentation.tools

data class ToolsState(
    val snackbarMessage: String? = null,
)

sealed interface ToolsIntent {
    data class ToolTapped(val tool: ToolItem) : ToolsIntent
    data object DismissSnackbar : ToolsIntent
}

sealed interface ToolsEffect {
    data class ShowSnackbar(val message: String) : ToolsEffect
}
```

### 6-B · `presentation/tools/ToolItem.kt`

```kotlin
package com.scandoc.presentation.tools

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ToolItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val bgTint: Color,
    val iconTint: Color,
)

data class ToolSection(
    val title: String,
    val items: List<ToolItem>,
)
```

### 6-C · `presentation/tools/ToolsCatalog.kt`

A singleton object that defines all sections and items:

```kotlin
package com.scandoc.presentation.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import com.scandoc.presentation.theme.ScanDocColors

object ToolsCatalog {
    val sections: List<ToolSection> = listOf(
        ToolSection(
            title = "Scanner",
            items = listOf(
                ToolItem("id_card", "ID Card", Icons.Outlined.Badge, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("extract_text", "Extract Text", Icons.Outlined.TextFields, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("id_photo", "ID Photo", Icons.Outlined.Person, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("formula", "Formula", Icons.Outlined.Functions, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("convert_photo", "Convert Photo", Icons.Outlined.PhotoFilter, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("book", "Book", Icons.Outlined.MenuBook, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("ppt", "PPT", Icons.Outlined.Slideshow, ScanDocColors.ToolTintOrange, ScanDocColors.ToolIconOrange),
                ToolItem("whiteboard", "Whiteboard", Icons.Outlined.Dashboard, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("timestamp", "Timestamp", Icons.Outlined.AccessTime, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
            ),
        ),
        ToolSection(
            title = "Import",
            items = listOf(
                ToolItem("import_images", "Import Images", Icons.Outlined.Image, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("import_files", "Import Files", Icons.Outlined.FolderOpen, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
            ),
        ),
        ToolSection(
            title = "Convert",
            items = listOf(
                ToolItem("to_word", "To Word", Icons.Outlined.Description, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("to_excel", "To Excel", Icons.Outlined.TableChart, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("to_ppt", "To PPT", Icons.Outlined.Slideshow, ScanDocColors.ToolTintOrange, ScanDocColors.ToolIconOrange),
                ToolItem("pdf_to_images", "PDF to Images", Icons.Outlined.Image, ScanDocColors.ToolTintPink, ScanDocColors.ToolIconPink),
                ToolItem("pdf_to_long", "PDF Long Image", Icons.Outlined.ViewDay, ScanDocColors.ToolTintPink, ScanDocColors.ToolIconPink),
            ),
        ),
        ToolSection(
            title = "Edit",
            items = listOf(
                ToolItem("signature", "Signature", Icons.Outlined.Draw, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("watermark", "Watermark", Icons.Outlined.Branding, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("eraser", "Smart Eraser", Icons.Outlined.AutoFixHigh, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("remove_marks", "Remove Marks", Icons.Outlined.CleaningServices, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("restore_photo", "Restore Photo", Icons.Outlined.RestoreFromTrash, ScanDocColors.ToolTintOrange, ScanDocColors.ToolIconOrange),
                ToolItem("merge", "Merge Files", Icons.Outlined.MergeType, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("extract_pages", "Extract Pages", Icons.Outlined.ContentCut, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("reorder_pages", "Reorder Pages", Icons.Outlined.SwapVert, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("lock", "Lock", Icons.Outlined.Lock, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("compress", "Compress", Icons.Outlined.Compress, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
            ),
        ),
        ToolSection(
            title = "Utilities",
            items = listOf(
                ToolItem("measure", "Measure", Icons.Outlined.Straighten, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
                ToolItem("solver_ai", "Solver AI", Icons.Outlined.AutoAwesome, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("count_cam", "CountCam", Icons.Outlined.Tag, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("scroll_capture", "Scroll Capture", Icons.Outlined.Screenshot, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("print", "Print", Icons.Outlined.Print, ScanDocColors.ToolTintBlue, ScanDocColors.ToolIconBlue),
                ToolItem("scan_3d", "3D Scan", Icons.Outlined.ViewInAr, ScanDocColors.ToolTintPurple, ScanDocColors.ToolIconPurple),
                ToolItem("qr_code", "QR Code", Icons.Outlined.QrCode, ScanDocColors.ToolTintTeal, ScanDocColors.ToolIconTeal),
            ),
        ),
    )
}
```

> **Note:** Some icon names (`Branding`) may not exist in Material Icons Outlined. If an icon is missing, substitute with the closest available (e.g. `Icons.Outlined.Verified` for Branding). Check `androidx.compose.material.icons.outlined.*` availability at compile time.

### 6-D · `presentation/tools/component/ToolTile.kt`

```kotlin
@Composable
fun ToolTile(
    item: ToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
)
```

Spec:
- Outer column: width = `ScanDocDimens.toolItemWidth` (76dp), centered horizontally
- Circle container: `ScanDocDimens.toolIconContainerSize` (56dp), background = `item.bgTint`, shape = `CircleShape`
- Icon inside circle: `ScanDocDimens.toolIconSize` (28dp), tint = `item.iconTint`
- Label below: `Text(item.label)`, 11sp, `FontWeight.Medium`, `color = ScanDocColors.ToolText`, max 2 lines, centered
- 8dp gap between circle and label
- Ripple click on whole column

### 6-E · `presentation/tools/component/ToolSectionHeader.kt`

```kotlin
@Composable
fun ToolSectionHeader(title: String, modifier: Modifier = Modifier)
```

Spec: `Text(title)`, 16sp `FontWeight.Bold`, color = `ScanDocColors.ToolText`, left-aligned.

### 6-F · `presentation/tools/ToolsViewModel.kt`

```kotlin
class ToolsViewModel : BaseViewModel<ToolsState, ToolsEffect>(ToolsState()) {
    fun onIntent(intent: ToolsIntent) {
        when (intent) {
            is ToolsIntent.ToolTapped -> emitEffect(
                ToolsEffect.ShowSnackbar("${intent.tool.label} — coming soon")
            )
            ToolsIntent.DismissSnackbar -> updateState { it.copy(snackbarMessage = null) }
        }
    }
}
```

Register `ToolsViewModel` in `PresentationModule.kt`:
```kotlin
viewModel { ToolsViewModel() }
```

### 6-G · `presentation/tools/ToolsScreen.kt`

```kotlin
@Composable
fun ToolsScreen(
    state: ToolsState,
    onIntent: (ToolsIntent) -> Unit,
    modifier: Modifier = Modifier,
)
```

Spec:
- Scaffold with light background `ScanDocColors.ToolSurface`
- `TopAppBar`: title "Tools", actions = search icon
- Body: `LazyColumn` with `contentPadding = PaddingValues(horizontal = 16.dp, bottom = 80.dp)`
- For each `ToolSection`:
  1. `ToolSectionHeader(section.title)` with 28dp top padding
  2. `FlowRow` (or chunked LazyRow rows) of `ToolTile`s — 4 items per row, equal spacing
  3. If using manual rows: `section.items.chunked(4).forEach { row -> Row { row.forEach { ToolTile(it) } } }`
- `SnackbarHost` shows `state.snackbarMessage` when non-null

> Use `androidx.compose.foundation.layout.FlowRow` if available in the compose version used (check `build.gradle.kts`). Otherwise use `chunked(4)` approach.

### Verification
- All 6 files compile.
- `ToolsViewModel` appears in `PresentationModule.kt`.
- Grep: `ToolsCatalog.sections` referenced in `ToolsScreen.kt`.

---

## Phase 7 — Me Screen (Stub)

**Goal:** Create a minimal profile/settings screen.  
**File to create:** `presentation/me/MeScreen.kt`

```kotlin
package com.scandoc.presentation.me

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scandoc.presentation.theme.ScanDocColors
import com.scandoc.presentation.theme.ScanDocDimens

@Composable
fun MeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScanDocColors.ToolSurface)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = ScanDocDimens.spaceMd),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(ScanDocDimens.space2xl))

        Text(
            text = "Me",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = ScanDocColors.ToolText,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(ScanDocDimens.space2xl))

        // Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(ScanDocColors.TealDim),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = ScanDocColors.Teal,
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(Modifier.height(ScanDocDimens.spaceMd))

        Text("Guest User", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = ScanDocColors.ToolText)
        Text("Sign in to sync your documents", fontSize = 13.sp, color = ScanDocColors.ToolTextSub)

        Spacer(Modifier.height(ScanDocDimens.space2xl))

        // Settings list
        val rows = listOf(
            Icons.Outlined.ManageAccounts to "Account",
            Icons.Outlined.Storage to "Storage",
            Icons.Outlined.Tune to "Preferences",
            Icons.Outlined.Info to "About",
        )
        rows.forEach { (icon, label) ->
            MeRow(icon = icon, label = label)
        }
    }
}

@Composable
private fun MeRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScanDocDimens.spaceMd),
    ) {
        Icon(icon, contentDescription = null, tint = ScanDocColors.ToolTextSub, modifier = Modifier.size(22.dp))
        Text(label, fontSize = 15.sp, color = ScanDocColors.ToolText, modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = ScanDocColors.ToolTextSub)
    }
    HorizontalDivider(color = ScanDocColors.ToolSurface.copy(alpha = 0.5f))
}
```

### Verification
- File compiles.
- `MeScreen` is callable from `App.kt` with no arguments.

---

## Phase 8 — Final Integration & Polish

**Goal:** Wire everything together in `App.kt`, verify the full compile, and fix any remaining issues.

### 8-A Checklist before starting
- [ ] Phase 1 complete: `ScanDocColors.Teal` and `ScanDocDimens.navBarHeight` exist
- [ ] Phase 2 complete: `NavTab`, `AppRoute.Home/Tools/Me`, `isCameraOverlayOpen` in AppState
- [ ] Phase 3 complete: `ScanDocBottomBar` composable exists
- [ ] Phase 4 complete: `App.kt` scaffold wired
- [ ] Phase 5 complete: Camera redesign complete
- [ ] Phase 6 complete: `ToolsScreen` + `ToolsViewModel` exist and registered in DI
- [ ] Phase 7 complete: `MeScreen` exists

### 8-B · DI Registration (`PresentationModule.kt`)

Verify these lines exist:
```kotlin
viewModel { LibraryViewModel(get(), get(), get(), get()) }
viewModel { CameraViewModel(get()) }
viewModel { CropViewModel(get(), get(), get()) }
viewModel { ViewerViewModel(get(), get()) }
viewModel { ToolsViewModel() }
viewModel { AppViewModel() }
```

### 8-C · AppViewModel — add Home/Tools/Me route handling

Update `onLibraryEffect` so it no longer calls `navigate(AppRoute.Camera)` — instead:
```kotlin
LibraryEffect.NavigateToCamera -> openCamera()
```

### 8-D · Run full compile
```
./gradlew :composeApp:compileKotlinAndroid
```

Fix all errors. Common issues to watch for:
- Missing imports for new composables
- `ToolsViewModel` not in DI module
- `MeScreen` import not added to `App.kt`
- Icon names that don't exist in `Icons.Outlined.*` — replace with nearest equivalent

### 8-E · Run Android debug unit tests
```
./gradlew :composeApp:testDebugUnitTest
```

All tests must pass green.

### Verification — Definition of Done
- [ ] `./gradlew :composeApp:compileKotlinAndroid` → BUILD SUCCESSFUL
- [ ] `./gradlew :composeApp:testDebugUnitTest` → all tests pass
- [ ] Grep: `ScanDocColors.Teal` used in at least `ScanDocBottomBar.kt`, `ShutterButton.kt`, `FeatureStrip.kt`
- [ ] Grep: `NavTab` referenced in `AppState.kt` and `App.kt`
- [ ] Grep: `ToolsViewModel` in `PresentationModule.kt`
- [ ] Grep: `isCameraOverlayOpen` in `App.kt`
- [ ] Grep: `AnimatedVisibility` used for camera overlay in `App.kt`
- [ ] No `!!` operator anywhere in new files (`grep -r "!!" presentation/tools presentation/me navigation/ScanDocBottomBar.kt`)
- [ ] No `import android.*` in any `commonMain` file

---

## File Creation Summary

| Phase | New Files | Edit Files |
|-------|-----------|------------|
| 1 | — | Color.kt, Dimens.kt |
| 2 | NavTab.kt | AppRoute.kt, AppState.kt, AppViewModel.kt |
| 3 | ScanDocBottomBar.kt | — |
| 4 | — | App.kt |
| 5 | CameraTopBar.kt, FeatureStrip.kt | CameraState.kt, CameraViewModel.kt, ModeSwitcher.kt, ShutterButton.kt, CameraScreen.kt |
| 6 | ToolsState.kt, ToolItem.kt, ToolsCatalog.kt, ToolTile.kt, ToolSectionHeader.kt, ToolsViewModel.kt, ToolsScreen.kt | PresentationModule.kt |
| 7 | MeScreen.kt | — |
| 8 | — | App.kt, AppViewModel.kt, PresentationModule.kt |

---

## Notes for Codex

- All new files go under `composeApp/src/commonMain/kotlin/com/scandoc/`
- Package names must match directory: `presentation/tools/` → `package com.scandoc.presentation.tools`
- Every `@Composable` must have `modifier: Modifier = Modifier` as last (or last-optional) parameter
- Use `ScanDocColors.*` for every color — never hardcode hex in composables
- Use `ScanDocDimens.*` for every spacing — never hardcode dp values except in this spec document
- Check icon availability before using — substitute closest available if missing
- Run `./gradlew :composeApp:compileKotlinAndroid` after EVERY phase
