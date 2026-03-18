plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    compileOnly("com.android.tools.lint:lint-api:32.1.0")
    compileOnly("com.android.tools.lint:lint-checks:32.1.0")
    compileOnly(kotlin("stdlib"))
    testImplementation("com.android.tools.lint:lint-tests:32.1.0")
}

configurations.runtimeElements {
    outgoing.artifacts.clear()
    outgoing.artifact(tasks.jar)
}

tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "it.sephiroth.android.library.asm.trunk.lint.TrunkIssueRegistry")
    }
}
