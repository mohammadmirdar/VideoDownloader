import com.android.build.gradle.LibraryExtension
import org.gradle.kotlin.dsl.getByType

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
}
fun String.isDomain(): Boolean = startsWith(":domain:")
fun String.isFeature(): Boolean = startsWith(":feature:")
fun String.isData(): Boolean = startsWith(":data:")
fun String.isComposeEnabled(): Boolean = isFeature() ||
        endsWith(":navigation") ||
        endsWith(":designsystem")