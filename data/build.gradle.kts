plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "${ProjectConfiguration.ApplicationId}.data"
    compileSdk = ProjectConfiguration.CompileSdk

    defaultConfig {
        minSdk = ProjectConfiguration.MinSdk
        targetSdk = ProjectConfiguration.TargetSdk
    }

    kotlin {
        jvmToolchain(libs.versions.jvm.get().toInt())
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
    ksp(libs.koin.ksp.compiler)

    // Retrofit
    api(libs.bundles.retrofit)
    api(libs.logging.interceptor)

    implementation(libs.coroutines.core)
    implementation(libs.kotlin.datetime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
