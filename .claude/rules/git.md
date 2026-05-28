# ScanDoc — Git Conventions

## Branch Strategy
- `main` — stable, always green CI
- `develop` — integration branch
- `claude/<phase>-<short-description>` — feature branches per phase
- `fix/<short-description>` — bug fixes

## Commit Message Format
Conventional Commits (https://www.conventionalcommits.org/):

```
<type>(<scope>): <short summary>

[optional body]

[optional footer]
```

### Types
| Type       | When to use                                      |
|------------|--------------------------------------------------|
| `feat`     | New feature or capability                        |
| `fix`      | Bug fix                                          |
| `refactor` | Code change with no behavior change              |
| `test`     | Adding or updating tests                         |
| `chore`    | Build system, tooling, dependency updates        |
| `docs`     | Documentation only                               |
| `style`    | Formatting, whitespace (no logic change)         |
| `perf`     | Performance improvement                          |

### Scopes (optional but recommended)
- `domain` — domain layer changes
- `data` — data layer changes
- `camera` — camera screen/platform bridge
- `crop` — crop screen
- `library` — library screen
- `viewer` — viewer screen
- `theme` — design system
- `di` — dependency injection
- `db` — SQLDelight schema/queries
- `ci` — CI/CD configuration

### Examples
```
feat(library): add search by OCR text

fix(crop): prevent corner handles from leaving image bounds

refactor(domain): extract Outcome sealed class to core module

test(usecase): add RunOcrUseCase unit tests before implementation

chore(deps): bump kotlin to 2.0.21

docs(architecture): add layer diagram to architecture.md
```

## Commit Discipline
- One logical change per commit — do not bundle unrelated changes
- Commit tests and implementation in the same commit (test-first still applies — write test, confirm it fails, implement, confirm it passes, commit both)
- Never commit: `.idea/`, `build/`, `local.properties`, `*.xcuserstate`, API keys
- Use `git add -p` (patch staging) to avoid accidental noise in commits

## Pull Request Rules
- PR title follows the same Conventional Commit format
- PR description includes: what changed, why, how to test
- Each PR targets one phase or logical unit of work
- CI must be green before merge
- No force-pushing to `main` or `develop`
