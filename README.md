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
- **Koin** — dependency injection (with annotations + KSP)
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
./gradlew test   # run unit tests
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
