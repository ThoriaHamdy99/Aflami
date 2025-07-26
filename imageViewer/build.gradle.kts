plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.aflami.custom.plugin)
}

android {
    namespace = "com.amsterdam.imageviewer"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))

    // Coil for image loading
    implementation(libs.coil.compose)

    // TensorFlow Lite for on-device ML
    implementation(libs.tensorflow.lite.task.vision)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ml.modeldownloader)

    implementation(libs.androidx.startup.runtime)
}