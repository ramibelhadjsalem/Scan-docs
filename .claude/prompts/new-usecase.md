# Template: Creating a New Use Case

Use this template when adding a new use case to ScanDoc.

## Rules (from architecture.md)
- Write the TEST first — confirm it fails — then write the implementation
- Use case lives in `domain/usecase/<category>/`
- Use case class has exactly ONE public method: `operator fun invoke(...)`
- Returns `Outcome<T>` — never throws
- Zero dependencies on Android, iOS, Compose, or any framework
- Only injects repository interfaces or platform `expect` classes (via interface)

## Checklist
- [ ] Test written first in `commonTest/.../usecase/`
- [ ] Test fails as expected (no implementation yet)
- [ ] Use case class created in `domain/usecase/<category>/`
- [ ] Koin binding added to `DomainModule.kt`
- [ ] Test passes after implementation
- [ ] ViewModel updated to inject and call the use case

## File Location
```
domain/usecase/<category>/
└── <Verb><Noun>UseCase.kt

commonTest/kotlin/com/scandoc/domain/usecase/
└── <Verb><Noun>UseCaseTest.kt
```

## Use Case Template

```kotlin
package com.scandoc.domain.usecase.<category>

import com.scandoc.core.result.Outcome
import com.scandoc.core.result.toOutcome

class <Verb><Noun>UseCase(
    private val repository: <Noun>Repository,
    // inject other dependencies here
) {
    suspend operator fun invoke(/* params */): Outcome<ResultType> =
        runCatching {
            repository.doSomething(/* params */)
        }.toOutcome()
}
```

## Test Template

```kotlin
package com.scandoc.domain.usecase.<category>

import com.scandoc.core.result.Outcome
import com.scandoc.fake.Fake<Noun>Repository
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class <Verb><Noun>UseCaseTest {

    private val fakeRepository = Fake<Noun>Repository()
    private val useCase = <Verb><Noun>UseCase(fakeRepository)

    @Test
    fun `should <expected result> when <happy path condition>`() = runTest {
        // Arrange
        // ...

        // Act
        val result = useCase(/* params */)

        // Assert
        assertIs<Outcome.Success<ResultType>>(result)
        // ...
    }

    @Test
    fun `should return failure when repository throws`() = runTest {
        // Arrange
        fakeRepository.shouldFail = true

        // Act
        val result = useCase(/* params */)

        // Assert
        assertIs<Outcome.Failure>(result)
    }
}
```

## Koin Registration
In `core/di/DomainModule.kt`:

```kotlin
factory { <Verb><Noun>UseCase(get()) }
```
