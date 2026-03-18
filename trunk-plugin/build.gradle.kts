import org.gradle.kotlin.dsl.provideDelegate
import java.util.Properties

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version libs.versions.kotlin
}

val rootProperties = Properties().apply {
    val propertiesFile = file("../gradle.properties")
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use { load(it) }
    }
}

val pluginVersion = rootProperties.getProperty("pluginVersion") ?: "0.0.1-SNAPSHOT"
val pluginGroupId = rootProperties.getProperty("pluginGroupId") ?: "com.github.sephiroth74.Trunk"


group = pluginGroupId
version = pluginVersion


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
