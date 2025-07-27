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
    firebaseDependencies()
    appModulesDependencies()
    koinDependencies()
    otherDependencies()
}

private fun DependencyHandlerScope.appModulesDependencies() {
    implementation(project(":ui"))
    implementation(project(":viewModel"))
    implementation(project(":localDatasource"))
    implementation(project(":remoteDatasource"))
    implementation(project(":domain"))
    implementation(project(":repository"))
}

fun DependencyHandlerScope.firebaseDependencies() {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.perf)
}

fun DependencyHandlerScope.koinDependencies() {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
}

fun DependencyHandlerScope.otherDependencies() {
    // json
    implementation(libs.kotlinx.serialization.json)

    // room
    implementation(libs.androidx.room.runtime)

    //manifest
    debugImplementation(libs.androidx.ui.test.manifest)

    //Datastore
    implementation(libs.androidx.datastore.preferences)
}