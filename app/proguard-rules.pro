############################################################
# General Android / Kotlin Rules
############################################################

# Keep class names and annotations used by reflection
-keepattributes Signature, InnerClasses, EnclosingMethod, Annotation
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Keep all @Keep-annotated classes and members
-keep @androidx.annotation.Keep class * { *; }

# Keep Kotlin metadata (important for reflection, Moshi, etc.)
-keepclassmembers class kotlin.Metadata { *; }

# Don’t warn about Kotlin/JVM internals
-dontwarn kotlin.**
-dontwarn org.jetbrains.annotations.**
-dontwarn kotlinx.coroutines.**

# Optimize safely
-optimizationpasses 5
-dontpreverify

############################################################
# Hilt / Dagger Rules
############################################################

# Keep Hilt-generated components and entry points
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.managers.* { *; }

# Keep entry points, modules, and injectables
-keep class * {
    @dagger.hilt.EntryPoint *;
    @dagger.Module *;
    @dagger.Provides *;
    @dagger.Binds *;
    @javax.inject.Inject *;
}

# Don't warn about Dagger/Hilt internals
-dontwarn dagger.hilt.internal.**
-dontwarn dagger.internal.codegen.**
-dontwarn javax.inject.**

############################################################
# Retrofit / OkHttp / Moshi / Arrow
############################################################

# Keep Retrofit interfaces
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**

# Keep Moshi adapters and generated code
-keep class com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonClass class * { *; }
-dontwarn com.squareup.moshi.**

# Arrow Retrofit / Arrow Core
-keep class arrow.** { *; }
-dontwarn arrow.**

# OkHttp internal logs
-dontwarn okhttp3.**
-keep class okhttp3.internal.** { *; }

############################################################
# Room Database
############################################################

# Keep models and DAO methods
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep class * implements androidx.room.RoomDatabase
-keep class * extends androidx.room.RoomOpenHelper
-keepclasseswithmembers class * {
    @androidx.room.* <methods>;
}

# Don’t warn about missing schema
-dontwarn androidx.room.paging.**

############################################################
# Jetpack Compose
############################################################

# Keep composable functions (needed for reflection and preview)
-keep class androidx.compose.** { *; }
-keep @androidx.compose.runtime.Composable class * { *; }
-keep class * implements androidx.compose.runtime.Composable { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-dontwarn androidx.compose.**

############################################################
# Firebase (Analytics & Crashlytics)
############################################################

-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Firebase Analytics events often use reflection
-keepclassmembers class * {
    @com.google.firebase.analytics.FirebaseAnalytics$Param *;
}

############################################################
# Coil
############################################################

-keep class coil.** { *; }
-dontwarn coil.**

############################################################
# Media3 / ExoPlayer
############################################################

-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

############################################################
# Adivery Ads SDK
############################################################

-keep class com.adivery.** { *; }
-dontwarn com.adivery.**

############################################################
# Chucker (Network Inspector)
############################################################

# Debug only — safe to suppress warnings
-dontwarn com.chuckerteam.chucker.**

############################################################
# Miscellaneous Libraries
############################################################

# Google Play Install Referrer
-keep class com.android.installreferrer.** { *; }

# Fetch Library
-keep class com.tonyodev.fetch.** { *; }
-dontwarn com.tonyodev.fetch.**

# Timber
-dontwarn timber.log.Timber
-keep class timber.log.Timber { *; }

############################################################
# Application Classes
############################################################

# Keep Application, Activities, ViewModels, Composables, and custom views
-keep class com.mirdar.videodownloader.** { *; }

############################################################
# End of Rules
############################################################
