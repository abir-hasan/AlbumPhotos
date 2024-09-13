plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

kotlin.sourceSets["main"].kotlin {
    srcDir("build/generated/ksp/main/kotlin")
}

dependencies{
    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    implementation(libs.coroutines.core)

    // Test
    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
    testImplementation(libs.jupiter)
    testRuntimeOnly(libs.jupiter.engine)
    testImplementation(libs.mockk.core)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.jeasy)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
