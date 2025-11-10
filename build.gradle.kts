import com.android.build.gradle.LibraryExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false
}

subprojects {
    if (path.isComposeEnabled()) {
        with(rootProject.libs) {
            pluginManager.apply(plugins.android.library.get().pluginId)
            pluginManager.apply(plugins.compose.compiler.get().pluginId)
            with(extensions.getByType<LibraryExtension>()) {
                buildFeatures {
                    compose = true
                }
            }
        }
    }

    project.plugins.configureAppAndModules(subProject = project)

}


fun PluginContainer.configureAppAndModules(subProject: Project) = apply {
    whenPluginAdded {
        when (this) {
            is com.android.build.gradle.AppPlugin -> {
                subProject.extensions
                    .getByType<com.android.build.gradle.AppExtension>()
                    .applyAppCommons()
            }

            is com.android.build.gradle.LibraryPlugin -> {
                subProject.extensions
                    .getByType<LibraryExtension>()
                    .applyLibraryCommons()
            }
        }
    }
}

fun com.android.build.gradle.AppExtension.applyAppCommons() = apply { applyBaseCommons() }


fun LibraryExtension.applyLibraryCommons() = apply {
    applyBaseCommons()
}

fun com.android.build.gradle.BaseExtension.applyBaseCommons() = apply {
    compileSdkVersion(libs.versions.compileSdk.get().toInt())

    defaultConfig.apply {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
    }

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

fun String.isDomain(): Boolean = startsWith(":domain:")
fun String.isFeature(): Boolean = startsWith(":feature:")
fun String.isData(): Boolean = startsWith(":data:")
fun String.isComposeEnabled(): Boolean = isFeature() ||
        endsWith(":navigation") ||
        endsWith(":designsystem")