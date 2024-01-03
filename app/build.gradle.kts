@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.onandoff.onandoff_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.onandoff.onandoff_android"
        minSdk = 23
        targetSdk = 34
        versionCode = 3
        versionName = "3"
        buildConfigField("String", "ON_AND_OFF_URL", "\"https://3.36.187.9:5050\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("debug") {
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = true
        }
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.material)

    // Paging
    implementation(libs.androidx.paging)

    // pagination 모듈
    implementation(libs.pagination)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.testjunit)
    androidTestImplementation(libs.espresso.core)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Coroutines
    implementation(libs.bundles.coroutines)

    implementation(libs.play.core)
    testImplementation(libs.androidx.arch.core)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Retrofit2
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.okhttp)

    // 말풍선 디자인
    implementation(libs.elastic.view)

    // 카카오 로그인 모듈
    implementation(libs.kakao)

    // 구글 로그인 모듈
    implementation(libs.google.login)

    // 알람을 위한 파이어베이스 설정
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.ted.keyboard.observer)

    implementation(libs.ksp)
}

kapt {
    correctErrorTypes = true
}