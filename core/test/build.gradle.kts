plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

dependencies {
    api(libs.jupiter)
    api(libs.coroutines.test)
    implementation(libs.coroutines.core)
    implementation(libs.mockk.core)
}
