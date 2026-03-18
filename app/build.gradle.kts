import it.sephiroth.android.library.asm.trunk.AsmLoggingPluginExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)

//    alias(libs.plugins.trunk)

    id("it.sephiroth.android.library.asm.trunk.plugin")
}

configure<AsmLoggingPluginExtension> {
    enabled = true
}

android {
    namespace = "it.sephiroth.logging.example"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "it.sephiroth.logging.example"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
//        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    implementation(libs.trunk.runtime)
    implementation(project(":trunk-runtime"))
}