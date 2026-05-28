# Phase 4/5 Foundation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Complete ScanDoc Phase 4 data persistence and Phase 5 domain use case wiring while enforcing the repository's strict architecture rules.

**Architecture:** Domain owns `Outcome` and pure platform contracts. Core platform `expect` classes implement those contracts at the platform boundary. Data implements repositories with SQLDelight and Okio-facing storage, then Koin wires data and domain modules.

**Tech Stack:** Kotlin Multiplatform, SQLDelight 2.0.2, Koin 4.0.0, Okio 3.9.1, kotlinx.coroutines/test, Turbine.

---

### Task 1: Restore Build Entry Point

**Files:**
- Create: `gradlew`
- Create: `gradlew.bat`
- Create: `gradle/wrapper/gradle-wrapper.jar`

- [x] **Step 1: Add the standard Gradle wrapper files**
- [x] **Step 2: Run wrapper smoke test**

Run: `./gradlew --version`

Expected: Gradle 8.9 prints version information.

- [ ] **Step 3: Commit**

```bash
git add gradlew gradlew.bat gradle/wrapper/gradle-wrapper.jar
git commit -m "chore(build): restore gradle wrapper"
```

### Task 2: Move Outcome Into Domain

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/scandoc/domain/result/Outcome.kt`
- Delete: `composeApp/src/commonMain/kotlin/com/scandoc/core/result/Outcome.kt`
- Modify imports under `composeApp/src/commonMain/kotlin/com/scandoc/`
- Modify imports under `composeApp/src/commonTest/kotlin/com/scandoc/`

- [ ] **Step 1: Change tests first**
- [ ] **Step 2: Run tests to verify red**
- [ ] **Step 3: Add domain result implementation**
- [ ] **Step 4: Update production imports and delete old file**
- [ ] **Step 5: Run tests**

### Task 3: Add Domain Platform Contracts

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/scandoc/domain/platform/CameraGateway.kt`
- Create: `composeApp/src/commonMain/kotlin/com/scandoc/domain/platform/ImageProcessor.kt`
- Create: `composeApp/src/commonMain/kotlin/com/scandoc/domain/platform/OcrEngine.kt`
- Create: `composeApp/src/commonMain/kotlin/com/scandoc/domain/platform/PdfExporter.kt`
- Create: `composeApp/src/commonMain/kotlin/com/scandoc/domain/platform/AppFileSystem.kt`
- Modify: use cases that currently import `com.scandoc.core.platform.*`
- Modify: common and actual `core/platform` bridge files
- Modify: OCR test fake import

- [ ] **Step 1: Change tests first**
- [ ] **Step 2: Run focused test to verify red**
- [ ] **Step 3: Add pure domain contracts**
- [ ] **Step 4: Update use cases and platform bridges**
- [ ] **Step 5: Run architecture grep**

### Task 4: Implement Data Mapping and Repositories

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/scandoc/data/local/mapper/DocumentMapper.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/scandoc/data/repository/DocumentRepositoryImpl.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/scandoc/data/storage/FileStorage.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/scandoc/data/repository/ImageRepositoryImpl.kt`

- [ ] **Step 1: Add mapper tests first**
- [ ] **Step 2: Run mapper tests red**
- [ ] **Step 3: Implement mapper helpers**
- [ ] **Step 4: Implement repositories**
- [ ] **Step 5: Run tests**

### Task 5: Wire DI Modules

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/scandoc/core/di/DataModule.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/scandoc/core/di/DomainModule.kt`

- [ ] **Step 1: Add DI compile expectation**
- [ ] **Step 2: Populate modules**
- [ ] **Step 3: Run metadata compile**

### Task 6: Final Verification and Commit

- [ ] **Step 1: Run full verification**
- [ ] **Step 2: Check architecture rules**
- [ ] **Step 3: Commit implementation**

