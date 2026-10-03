plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.koin.compiler)
}

android {
    namespace = "${ProjectConfiguration.ApplicationId}.presentation"
    compileSdk = ProjectConfiguration.CompileSdk

    defaultConfig {
        minSdk = ProjectConfiguration.MinSdk
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    kotlin {
        jvmToolchain(
            libs.versions.jvm
                .get()
                .toInt(),
        )
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.annotations)

    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.atomicfu)

    // Test
    testImplementation(project(":core:test"))
    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
    testImplementation(libs.jupiter)
    testRuntimeOnly(libs.jupiter.engine)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.jeasy)
    testImplementation(libs.turbine)

    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
