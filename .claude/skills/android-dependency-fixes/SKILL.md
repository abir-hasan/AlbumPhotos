---
name: android-dependency-fixes
description: How to verify and fix a dependency update in the AlbumPhotos Android project - the Gradle verification command, transient-failure retries, where to research breaking changes, known fix patterns, and guardrails. Preloaded by the renovate-pr-fixer agent.
user-invocable: false
---

# Android dependency fixes

## Project layout

- Modules: `app`, `data`, `domain` (pure Kotlin/JVM), `presentation`, `core` (shared test helpers
  in `core/test`).
- Versions: `gradle/libs.versions.toml`. Several libraries share one `version.ref`, e.g. both Coil
  artifacts use `coil-compose`.
- SDK levels: `buildSrc/src/main/kotlin/ProjectConfiguration.kt` (`CompileSdk`, `TargetSdk`,
  `MinSdk`).
- DI: Koin annotations processed by the Koin Compiler Plugin (`io.insert-koin.compiler.plugin`).
  There is no KSP.
- Lint: detekt 1.x with compose-rules (`config/detekt/detekt.yml`), and ktlint (`.editorconfig`).
- Renovate config: `renovate.json`.

## Verify

```bash
./gradlew clean assembleDebug test detekt ktlintCheck --continue > /tmp/renovate-pr-<N>-verify.log 2>&1
echo "exit=$?"
```

Exit code 0 means it passes. On failure, read only the relevant parts:

```bash
grep -E "^e:|What went wrong" -A10 /tmp/renovate-pr-<N>-verify.log | head -80
```

**Transient failures** (HTTP 5xx, timeouts, `Could not GET`, Gradle wrapper download errors): rerun
the same command after 30s, then 60s, 90s and so on, up to 5 times. These retries are not fix
attempts. If downloads still fail after 5 retries, record the PR as `failed` with root cause
"network: could not download <what>" and don't spend fix attempts on it.

## Diagnose

1. Find the first real error in the log: compiler `e:` lines, task failures, AAR metadata
   messages, detekt or ktlint findings.
2. Link it to the bumped dependency. Read its release notes, migration guide and compatibility
   matrix:
   - GitHub releases: `gh api repos/<owner>/<repo>/releases --jq '.[0:5][] | {tag_name, body}'`
   - Maven Central versions: `https://repo1.maven.org/maven2/<group/path>/<artifact>/maven-metadata.xml`
   - Official docs, via WebFetch.
3. Cite the sources you used in the result record.

## Known fix patterns (seen in this project)

| Symptom | Fix |
|---|---|
| `checkDebugAarMetadata`: "requires … compile against version N or later" | Raise `CompileSdk` in `ProjectConfiguration.kt`. Never change `MinSdk` or `TargetSdk`. AGP installs the SDK platform itself. |
| Renovate chose a side artifact, e.g. `0.8.0-0.6.x-compat` | Use the real release version (`0.8.0`) and add `allowedVersions: "!/-compat$/"` for that package in `renovate.json`. |
| New version needs a newer tool we don't have (e.g. compose-rules ≥0.5 needs detekt 2.x) | Hold at the newest compatible version and add a matching `allowedVersions` rule to `renovate.json`, with a description saying when to remove it. Record it as a follow-up. |
| Feature split into a separate artifact (e.g. Coil 3 network loading needs `coil-network-okhttp`) | Add the companion artifact to the catalog using the same `version.ref`, and add it to the module. |
| Annotation, package or API moved (e.g. `@KoinViewModel` → `org.koin.core.annotation`) | Migrate the code and imports, following the library's migration guide. |
| Only ktlint formatting fails (import order, blank lines) | `./gradlew ktlintFormat`, then verify again. |
| Kotlin or AGP major bump breaks a plugin | Check the plugin's compatibility notes, and bump the plugin to a version that supports the new Kotlin/AGP. |

## Guardrails (never)

- Disable, skip, or delete tests or checks, or remove tasks from the verification command.
- Add `@Suppress`, detekt baselines, or ktlint ignores just to make checks pass.
- Change `MinSdk` or `TargetSdk`.
- Edit `.github/workflows/*`.
- Force-push, or push or touch any branch other than the PR's own branch.
- Downgrade the dependency the PR updates, except under the hold rule above, and then always with
  a `renovate.json` rule plus a follow-up.
- Make unrelated refactors. Keep changes minimal and match the surrounding style.
