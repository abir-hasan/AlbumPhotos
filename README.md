# Album Photos

A sample multi-module Android app (Jetpack Compose) listing albums and their photos.

## Code quality

This project uses **detekt** (code smells) and **ktlint** (formatting). Both run on every pull
request and can be checked locally:

```bash
./gradlew detekt        # run detekt
./gradlew ktlintCheck --continue    # run ktlint
./gradlew ktlintFormat   # auto-fix ktlint issues
```

Config lives in `config/detekt/detekt.yml` (detekt) and `.editorconfig` (ktlint).
