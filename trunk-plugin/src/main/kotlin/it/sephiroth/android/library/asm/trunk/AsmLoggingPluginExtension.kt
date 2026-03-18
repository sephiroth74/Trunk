package it.sephiroth.android.library.asm.trunk

abstract class AsmLoggingPluginExtension {
    var enabled: Boolean = true

    companion object {
        val EXTENSION_NAME = "trunk"
    }
}