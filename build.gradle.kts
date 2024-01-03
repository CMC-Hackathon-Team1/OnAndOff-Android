// Top-level build file where you can add configuration options common to all sub-projects/modules.
@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.gms.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.devtools.ksp) apply false
}

extra.apply {
    set("activity_ktx_version", "1.8.2")
    set("fragment_ktx_version", "1.6.2")
    set("retrofit_version", "2.9.0")
    set("okhttp_version", "4.11.0")
    set("coroutines_version", "1.7.1")
    set("hilt_version", "2.48.1")
    set("paging_version", "3.1.1")
    set("glide_version", "4.14.2")
    set("room_version", "2.4.2")
    set("kakao_version", "2.15.0")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
