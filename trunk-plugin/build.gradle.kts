import org.gradle.kotlin.dsl.provideDelegate

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version libs.versions.kotlin
}

group = "com.github.sephiroth74.Trunk"
version = "0.0.1-SNAPSHOT"

gradlePlugin {
    plugins {
        create("trunkPlugin") {
            id = "it.sephiroth.android.library.asm.trunk.plugin"
            implementationClass = "it.sephiroth.android.library.asm.trunk.AsmLoggingPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.gradle)
    implementation(libs.asm)
    implementation(libs.asm.commons)
    implementation(kotlin("stdlib"))
}
