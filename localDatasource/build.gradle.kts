plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.kover)
    id("de.mannodermaus.android-junit5")
}

android {
    namespace = "com.example.localdatasource"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(project(":repository"))

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.androidx.room.testing)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Date and Time
    implementation(libs.kotlinx.datetime)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth.v101)
    testImplementation(kotlin("test"))

    // android test junit 5
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.junit.jupiter.api.v572)
    androidTestImplementation(libs.android.test.core)
    androidTestRuntimeOnly(libs.android.test.runner)
    androidTestImplementation(libs.truth.v101)

    androidTestImplementation(libs.kotlinx.coroutines.test.v171)
}