import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
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

        buildConfigField(
            "String",
            "KEY_BASE_URL",
            "\"http://192.168.1.103:8080/api/\""
        )

        buildConfigField(
            "String",
            "KEY_DEVICE_TYPE",
            "\"mobile\""
        )

        buildConfigField(
            "String",
            "ADIVERY_ID",
            "\"fd2d728f-438d-425f-b8f8-99a1601f32f2\""
        )

        buildConfigField(
            "String",
            "ADIVERY_REWARD_ID",
            "\"3eccb233-f711-4baf-b919-fd7bf3e657a4\""
        )

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
        buildConfig = true
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

        implementation(adivery)

        // Test
        testImplementation(junit)
        testImplementation(konsist)
        testImplementation(mockito.kotlin)
        testImplementation(mockK)
        testImplementation(coroutine.test)
        testImplementation(turbine)

        // Chucker
        debugImplementation(chucker.debug)
        releaseImplementation(chucker.release)

    }
}