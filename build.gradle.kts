// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}
buildscript {
    repositories {
        google()
    }
    buildscript {
        dependencies {
            classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.2")
        }
    }
}