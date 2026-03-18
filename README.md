# Trunk
Android logging plugin and runtime


## Installation

Edit the root **setting.gradle.kts** file to include the jitpack repository:

```groovy

pluginManagement {
    repositories {
        ...
        maven { url = uri("https://jitpack.io") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "it.sephiroth.android.library.asm.trunk.plugin") {
                useModule("com.github.sephiroth74.Trunk:trunk-plugin:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    ...
    repositories {
        ...
        maven { url = uri("https://jitpack.io") }
    }
}

```

then add the plugin and the runtime dependency to the **libs.versions.toml**:

```toml
[versions]
trunk = "0.0.3" # see the jitpack version

[libraries]
trunk-runtime = { module = "com.github.sephiroth74.Trunk:trunk-runtime", version.ref = "trunk" }

[plugins]
trunk = { id = "it.sephiroth.android.library.asm.trunk.plugin", version.ref = "trunk" }
```

Now, in your app's **build.gradle.kts** add the trunk-plugin in the plugins section:

```groovy
plugins {
    ...
    alias(libs.plugins.trunk)
}

configure<AsmLoggingPluginExtension> {
    enabled = true
}
```

and, in the **dependencies** section set the following:

```groovy

dependencies {
    ...
    implementation(libs.trunk.runtime)
}
```