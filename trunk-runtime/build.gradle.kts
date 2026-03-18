import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
    signing
}

val pluginVersion: String by project
val pluginGroupId: String by project


android {
    namespace = "it.sephiroth.android.library.asm.trunk.runtime"
    compileSdk {
        version = release(35)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "java.lang.String",
            "BUILD_DATE",
            "\"${
                SimpleDateFormat("yyyy-MM-dd").format(
                    Date()
                )}\"",
        )
    }

    buildFeatures {
        buildConfig = true
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    lintPublish(project(":trunk-lint"))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = pluginGroupId
                artifactId = "trunk-runtime"
                version = pluginVersion
            }
        }
    }
}