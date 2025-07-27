import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val movieSignUp: String = properties.getProperty("movieSignUp") ?: ""
val movieResetPassword: String = properties.getProperty("movieResetPassword") ?: ""

plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.amsterdam.viewmodel"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "MOVIE_SIGN_UP_URL",
            movieSignUp
        )
        buildConfigField(
            "String",
            "MOVIE_RESET_PASSWORD_URL",
            movieResetPassword
        )
    }
}

dependencies {
    modulesDependencies()
    coroutinesDependencies()
    otherDependencies()
    testDependencies()
}

private fun DependencyHandlerScope.modulesDependencies() {
    api(project(":domain"))
}

private fun DependencyHandlerScope.coroutinesDependencies() {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}

private fun DependencyHandlerScope.otherDependencies() {
    //kotlin extensions
    implementation(libs.androidx.core.ktx)

    //lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //datetime
    implementation(libs.kotlinx.datetime)

    // page 3
    implementation(libs.androidx.paging.runtime)
}

private fun DependencyHandlerScope.testDependencies() {
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))
}