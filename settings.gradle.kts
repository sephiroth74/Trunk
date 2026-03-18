pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }

//    resolutionStrategy {
//        eachPlugin {
//            if (requested.id.id == "it.sephiroth.android.library.asm.trunk.plugin") {
//                useModule("com.github.sephiroth74:Trunk:trunk-plugin-SNAPSHOT")
//            }
//        }
//    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Trunk"

includeBuild("trunk-plugin")
include(":trunk-runtime")
include(":app")
