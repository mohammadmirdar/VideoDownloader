import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.mirdar.videodownloader"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mirdar.videodownloader"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    with(projects) {
        implementation(designsystem)
    }

    with(libs) {
        implementation(coil)
        implementation(coil.network)
        implementation(moshi)
        implementation(timber)
        implementation(arrow.retrofit)
        implementation(bundles.okhttp)
        implementation(ads.identifier)
        implementation(bundles.media3)
        implementation(installreferrer)
        implementation(profileinstaller)
        implementation(bundles.retrofit)
        implementation(kotlin.immutable)

        implementation(platform(firebase.bom))
        implementation(firebase.crashlytics)
        implementation(firebase.analytics)

        // Hilt
        kapt(hilt.compiler)
        kapt(androidx.hilt.compiler)
        implementation(hilt.android)
        implementation(hilt.common)
        implementation(hilt.compose)

        // Compose
        implementation(bundles.compose)
        implementation(platform(compose.bom))
        debugImplementation(compose.ui.tooling)

        // Test
        testImplementation(junit)
        testImplementation(konsist)
        testImplementation(mockito.kotlin)
        testImplementation(mockK)
        testImplementation(coroutine.test)
        testImplementation(turbine)

        implementation(compose.navigation)
        implementation(kotlinx.serialization.json)
    }
}