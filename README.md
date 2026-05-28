# ScanDoc

A privacy-first, on-device document scanner for Android & iOS built with Kotlin Multiplatform + Compose Multiplatform.

## Features

- Live camera with real-time document edge detection
- Auto-capture when document is steady and framed
- Crop with draggable corners and perspective correction
- Filters: Auto / Doc B&W / Ink / Photo / Grayscale
- On-device OCR (ML Kit on Android, Apple Vision on iOS)
- Save as searchable PDF or images
- Document library with full-text search across OCR'd content
- 100% offline — no network calls, no accounts, no cloud

## Architecture

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

Dependencies point **inward only**: `Presentation → Domain ← Data ← Platform`.

## Tech Stack

| Concern        | Library                          |
|----------------|----------------------------------|
| UI             | Compose Multiplatform 1.7.x      |
| Language       | Kotlin 2.0.x (KMP)               |
| DI             | Koin 4.x                         |
| Database       | SQLDelight 2.x                   |
| File I/O       | Okio 3.x                         |
| Navigation     | Decompose 3.x                    |
| Async          | Kotlin Coroutines + Flow         |
| Android OCR    | ML Kit Text Recognition          |
| iOS OCR        | Apple Vision framework           |
| Android Camera | CameraX 1.4.x                    |
| iOS Camera     | AVFoundation                     |

## Project Structure

```
composeApp/src/
├── commonMain/kotlin/com/scandoc/
│   ├── core/           shared utilities, DI, expect declarations
│   ├── domain/         models, repository interfaces, use cases
│   ├── data/           repository implementations, DB, storage
│   └── presentation/   screens, viewmodels, theme, components
├── commonTest/         unit tests (domain + data)
├── androidMain/        Android actuals + entry point
└── iosMain/            iOS actuals
```

## Setup

### Prerequisites
- Android Studio Hedgehog or later
- Xcode 15 or later (for iOS)
- JDK 17+

### Android
1. Clone the repo
2. Open in Android Studio
3. Run the `composeApp` configuration on an Android device or emulator (API 26+)

### iOS
1. Clone the repo
2. Run `./gradlew :composeApp:generateSqlDelightInterface` to generate DB code
3. Open `iosApp/iosApp.xcodeproj` in Xcode
4. Run on a simulator or device (iOS 16+)

## Development Rules

All coding conventions, architecture rules, testing guidelines, and git conventions live in `.claude/`:

- `.claude/CLAUDE.md` — master rules
- `.claude/rules/architecture.md` — clean architecture guide
- `.claude/rules/kotlin-style.md` — Kotlin conventions
- `.claude/rules/compose-style.md` — Compose Multiplatform rules
- `.claude/rules/testing.md` — testing philosophy and patterns
- `.claude/rules/git.md` — commit and branch conventions
- `.claude/prompts/` — templates for new screens, use cases, repositories

## License

Private — all rights reserved.
