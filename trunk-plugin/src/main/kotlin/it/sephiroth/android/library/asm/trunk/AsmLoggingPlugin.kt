package it.sephiroth.android.library.asm.trunk

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope

class AsmLoggingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        val extension = project.extensions.findByType(AsmLoggingPluginExtension::class.java) ?: project.extensions.create(
            AsmLoggingPluginExtension.EXTENSION_NAME,
            AsmLoggingPluginExtension::class.java
        )

        androidComponents.onVariants { variant ->
            if (extension.enabled) {
                variant.instrumentation.transformClassesWith(
                    LoggingClassVisitorFactory::class.java,
                    InstrumentationScope.PROJECT
                ) {}
                variant.instrumentation.setAsmFramesComputationMode(
                    FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
                )
            }
        }
    }
}
