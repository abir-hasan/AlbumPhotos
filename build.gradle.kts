plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinx.serialization.plugin) apply false
    alias(libs.plugins.koin.compiler) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    val libs = rootProject.extensions
        .getByType<org.gradle.api.artifacts.VersionCatalogsExtension>()
        .named("libs")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    }

    extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        filter {
            // Generated sources (e.g. KSP/Koin) are not ours to format.
            exclude { it.file.path.contains("/generated/") }
        }
    }

    dependencies {
        "detektPlugins"(libs.findLibrary("detekt-compose-rules").get())
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
