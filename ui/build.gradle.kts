plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.amsterdam.ui"
}

dependencies {
    modulesDependencies()
    navigationDependencies()
    otherDependencies()
}

private fun DependencyHandlerScope.modulesDependencies() {
    implementation(project(":designSystem"))
    implementation(project(":viewModel"))
    implementation(project(":imageViewer"))
}

private fun DependencyHandlerScope.navigationDependencies() {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.common.android)
}

private fun DependencyHandlerScope.otherDependencies() {
    //android lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //activity compose
    implementation(libs.androidx.activity.compose)

    //compose bom
    implementation(platform(libs.androidx.compose.bom))

    //ui
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)

    //previe
    debugImplementation(libs.androidx.ui.tooling.preview)

    //app compact
    implementation(libs.androidx.appcompat)

    //foundation
    implementation(libs.androidx.foundation)

    //kotlin extensions
    implementation(libs.androidx.core.ktx)

    // page 3
    implementation(libs.androidx.paging.compose)

    //koin androidx compose
    implementation(libs.koin.androidx.compose)
}
