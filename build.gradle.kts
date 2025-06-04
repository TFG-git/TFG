// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
buildscript {
    repositories {
        // Check that you have the following line (if not, add it):
        google()  // Google's Maven repository
        mavenCentral()
    }
    dependencies {
        // Add this line
        classpath("com.google.gms:google-services:4.3.3")
    }
}

allprojects {
    repositories {
        // Check that you have the following line (if not, add it):
        // google()  // Google's Maven repository
    }
}