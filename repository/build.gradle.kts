import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val baseImageUrl: String = properties.getProperty("baseImageUrl") ?: ""

plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.amsterdam.repository"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"

        buildConfigField(
            "String",
            "BASE_IMAGE_URL",
            baseImageUrl
        )
    }
}

dependencies {

    modulesDependencies()
    roomDependencies()
    testDependencies()
    otherDependencies()
}

private fun DependencyHandlerScope.modulesDependencies() {
    api(project(":domain"))

}

private fun DependencyHandlerScope.roomDependencies() {
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}

private fun DependencyHandlerScope.testDependencies() {
    testImplementation(libs.junit.jupiter)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

private fun DependencyHandlerScope.otherDependencies() {
    //kotlin extensions
    implementation(libs.androidx.core.ktx)

    //app compact
    implementation(libs.androidx.appcompat)

    //coroutines
    implementation(libs.kotlinx.coroutines.core)

    // date time
    implementation(libs.kotlinx.datetime)

    // json serialization
    implementation(libs.kotlinx.serialization.json)
}