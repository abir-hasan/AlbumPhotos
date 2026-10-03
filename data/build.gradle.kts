plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.koin.compiler)
}

android {
    namespace = "${ProjectConfiguration.ApplicationId}.data"
    compileSdk = ProjectConfiguration.CompileSdk

    defaultConfig {
        minSdk = ProjectConfiguration.MinSdk
    }

    kotlin {
        jvmToolchain(
            libs.versions.jvm
                .get()
                .toInt(),
        )
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        all {
            buildConfigField("String", "AlbumBaseUrl", "\"https://jsonplaceholder.typicode.com\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)

    // Retrofit
    api(libs.bundles.retrofit)
    api(libs.logging.interceptor)

    implementation(libs.coroutines.core)
    implementation(libs.kotlin.datetime)

    // Test
    testImplementation(project(":core:test"))
    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
    testImplementation(libs.jupiter)
    testRuntimeOnly(libs.jupiter.engine)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)

    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Testing with JUnit5
tasks.withType<Test> {
    useJUnitPlatform()
}
