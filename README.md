# Trunk
Android logging plugin and runtime

Trunk is a lightweight Android logging library that uses ASM (Bytecode manipulation) to automatically inject tags and line numbers into your logs at compile time.

## How it works

The `trunk-plugin` transforms your code during the build process. When it encounters a call to `Trunk.d("message")`, it automatically:
1.  Determines the simple class name where the call is made.
2.  Captures the current line number.
3.  Replaces the call with a version that includes this metadata.

This means you get detailed logs without manually typing tags or using expensive reflection/stack trace inspection at runtime.

### Features
- **Automatic Tagging**: Uses the class name as the default tag.
- **Line Numbers**: Automatically includes the line number in the log output.
- **Zero Overhead (Runtime)**: No stack trace inspection at runtime to find the caller.
- **Timber-like API**: Familiar and easy-to-use API.
- **`once` logging**: Easily log a message only the first time it's encountered.

## Installation

Edit the root **settings.gradle.kts** file to include the jitpack repository and the plugin resolution strategy:

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
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
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Then add the plugin and the runtime dependency to your **libs.versions.toml**:

```toml
[versions]
trunk = "0.0.3" # check latest version on jitpack

[libraries]
trunk-runtime = { module = "com.github.sephiroth74.Trunk:trunk-runtime", version.ref = "trunk" }

[plugins]
trunk = { id = "it.sephiroth.android.library.asm.trunk.plugin", version.ref = "trunk" }
```

Now, in your app's **build.gradle.kts** apply the plugin:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.trunk)
}

// Optional configuration
trunk {
    enabled = true
}

dependencies {
    implementation(libs.trunk.runtime)
}
```

## Usage

### Basic Logging

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Trunk.d("Activity created")
        Trunk.i("User ID: %d", 123)
        
        try {
            // ...
        } catch (e: Exception) {
            Trunk.e(e, "Something went wrong")
        }
    }
}
```

**Output in Logcat:**
```text
D/MainActivity: [12] Activity created
I/MainActivity: [13] User ID: 123
E/MainActivity: [18] Something went wrong
java.lang.Exception: ...
```

### Log Once
Useful for logging inside loops or frequently called methods without flooding the logcat.

```kotlin
Trunk.once(1, Log.INFO, "This will be logged only once")
```

### Custom Tags

```kotlin
Trunk.tag("MyCustomTag").v("Hello World")
```

**Output:**
```text
V/MyCustomTag: [25] Hello World
```

## Build Information
The runtime library includes the build date:
```kotlin
Log.d("Trunk", "Library Build Date: ${it.sephiroth.android.library.asm.trunk.runtime.BuildConfig.BUILD_DATE}")
```

## License
[MIT License](LICENSE)
