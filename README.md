# Album Photos

A sample multi-module Android app (Jetpack Compose) listing albums and their photos.

## Architecture

The project follows Clean Architecture, split across Gradle modules with dependencies pointing
inward toward `:domain`:

- **`:domain`** — pure Kotlin. Use cases, domain models, and repository *interfaces*. Knows nothing
  about Android, networking, or UI.
- **`:data`** — implements the domain repository interfaces; handles networking, caching, and
  mapping to domain models. Depends on `:domain`.
- **`:presentation`** — ViewModels, UI state, and UI models/mappers. Depends on `:domain`.
- **`:app`** — Compose UI, navigation, and DI wiring. Depends on the modules above.

`:domain` is the center and has no module dependencies; `:data` and `:presentation` depend only on
it, keeping business logic isolated from frameworks.

## Libraries

- **Jetpack Compose** + **Material 3** — UI
- **Koin** — dependency injection (with annotations + Koin Compiler Plugin)
- **Retrofit** + **OkHttp** — networking
- **Coil** — image loading
- **kotlinx** — coroutines, serialization, datetime, immutable collections

## Testing

Unit tests run on the JVM with:

- **JUnit 5 (Jupiter)** — test runner
- **MockK** — mocking
- **Turbine** — testing Kotlin `Flow`
- **kotlinx-coroutines-test** — coroutine test dispatchers (shared `CoroutinesExtension` in `:core:test`)
- **easy-random** — test data generation

```bash
./gradlew test   # run unit tests on all the modules
```

## Code quality

This project uses **detekt** (code smells) and **ktlint** (formatting). Both run on every pull
request and can be checked locally:

```bash
./gradlew detekt        # run detekt
./gradlew ktlintCheck --continue    # run ktlint
./gradlew ktlintFormat   # auto-fix ktlint issues
```

Config lives in `config/detekt/detekt.yml` (detekt) and `.editorconfig` (ktlint).

## Dependency updates

**Renovate** keeps dependencies up to date, running weekly via a GitHub Action and opening
PRs against `main`: one PR per library, with libraries released together (e.g. Kotlin, Compose,
Koin) grouped. Config is in `renovate.json`.

### Fixing Renovate PRs

The `renovate-pr-fixer` Claude Code agent goes through every open Renovate PR one at a time. For
each PR it merges `main`, runs `./gradlew clean assembleDebug test detekt ktlintCheck`, makes up to
3 fix attempts, pushes working fixes and comments on the PR. PRs it can't fix get the
`needs-manual-fix` label. A dated report (`reports/renovate/renovate-fixes-YYYY-MM-DD.md`) is
written only when PRs were changed or failed.

Trigger it with either:

- `@agent-renovate-pr-fixer fix the open Renovate PRs` in any Claude Code session
- `claude --agent renovate-pr-fixer` from the terminal

Add "dry run" to do everything locally without pushing, commenting or labelling, or name PRs
("only #12 #14") to limit it.

The agent (`.claude/agents/renovate-pr-fixer.md`) is the "who". It preloads three skills from
`.claude/skills/` as the "how": `renovate-pr-workflow`, `android-dependency-fixes` and
`renovate-fix-report`.
