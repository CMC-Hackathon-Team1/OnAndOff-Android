// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.gms.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.legacy.kapt) apply false
    alias(libs.plugins.devtools.ksp) apply false
}

tasks.register<Delete>("clean") {
    description = "Deletes the root build directory."
    group = "build"

    delete(layout.buildDirectory)
}
