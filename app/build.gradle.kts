import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.legacy.kapt)
}

val supabaseProperties: Properties = Properties().also { properties ->
    rootProject.file("local.properties").inputStream().use { input ->
        properties.load(input)
    }
}

fun supabaseConfig(name: String): String {
    val value = requireNotNull(supabaseProperties.getProperty(name)) {
        "local.properties에 $name 설정이 필요합니다."
    }.trim()
    require(value.isNotEmpty()) { "$name 값이 비어 있습니다." }

    return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
}

android {
    namespace = "com.onandoff.onandoff_android"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.onandoff.onandoff_android"
        minSdk = 26
        targetSdk = 37
        versionCode = 11
        versionName = "11"
        buildConfigField("String", "SUPABASE_URL", supabaseConfig("SUPABASE_URL"))
        buildConfigField(
            "String",
            "SUPABASE_PUBLISHABLE_KEY",
            supabaseConfig("SUPABASE_PUBLISHABLE_KEY")
        )
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore.jks")
            storePassword = file("keystore_password.txt").readText().trim()
            keyAlias = file("key_alias.txt").readText().trim()
            keyPassword = file("key_password.txt").readText().trim()
        }
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = file("keystore_password.txt").readText().trim()
            keyAlias = file("key_alias.txt").readText().trim()
            keyPassword = file("key_password.txt").readText().trim()
        }
    }

    buildTypes {
        getByName("debug") {
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            proguardFile("proguard-kakao.pro")
            proguardFile("proguard-google.pro")
            proguardFile("proguard-retrofit2.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
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

//    implementation(libs.play.core)

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

}

kapt {
    correctErrorTypes = true
}
