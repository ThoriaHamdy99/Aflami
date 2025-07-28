plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.service)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.pref)
}

android {
    namespace = "com.amsterdam.aflami"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.amsterdam.aflami"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
        }
        debug {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    appModulesDependencies()
    firebaseDependencies()
    koinDependencies()
    datastoreDependencies()
    jsonSerializationDependencies()
    roomDependencies()
    androidxRuntimeDependencies()
}

private fun DependencyHandlerScope.appModulesDependencies() {
    implementation(project(":ui"))
    implementation(project(":viewModel"))
    implementation(project(":localDatasource"))
    implementation(project(":remoteDatasource"))
    implementation(project(":domain"))
    implementation(project(":repository"))
}

private fun DependencyHandlerScope.firebaseDependencies() {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.perf)
}

private fun DependencyHandlerScope.koinDependencies() {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.core)
}

private fun DependencyHandlerScope.datastoreDependencies() {
    implementation(libs.androidx.datastore.preferences)
}

private fun DependencyHandlerScope.jsonSerializationDependencies() {
    implementation(libs.kotlinx.serialization.json)
}

private fun DependencyHandlerScope.roomDependencies() {
    implementation(libs.androidx.room.runtime)
}

fun DependencyHandlerScope.androidxRuntimeDependencies() {
    implementation(libs.androidx.runtime)
}
