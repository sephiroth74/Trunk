package it.sephiroth.android.library.asm.trunk

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.slf4j.LoggerFactory

abstract class LoggingClassVisitorFactory : AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        return LoggingClassVisitor(nextClassVisitor, classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return !classData.className.startsWith("it.sephiroth.android.library.asm.runtime.logging")
    }
}

class LoggingClassVisitor(nextVisitor: ClassVisitor, private val className: String) : ClassVisitor(Opcodes.ASM9, nextVisitor) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return LoggingMethodVisitor(mv, className)
    }
}



class LoggingMethodVisitor(nextVisitor: MethodVisitor, private val className: String) : MethodVisitor(Opcodes.ASM9, nextVisitor) {
    private var currentLineNumber: Int = -1
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun visitLineNumber(line: Int, start: org.objectweb.asm.Label?) {
        currentLineNumber = line
        super.visitLineNumber(line, start)
    }

    override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {

        if (owner == TRUNK_CLASS_NAME && opcode == Opcodes.INVOKESTATIC) {
            val simpleClassName = className.substringAfterLast('.').replace('$', '.')

            if (name == "once") {
                when (descriptor) {
                    "(IILjava/lang/String;[Ljava/lang/Object;)V" -> {
                        super.visitLdcInsn(simpleClassName)
                        super.visitIntInsn(Opcodes.SIPUSH, currentLineNumber)
                        super.visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            owner,
                            "logOnce",
                            "(IILjava/lang/String;[Ljava/lang/Object;Ljava/lang/String;I)V",
                            false
                        )
                        return
                    }

                    "(IILjava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V" -> {
                        super.visitLdcInsn(simpleClassName)
                        super.visitIntInsn(Opcodes.SIPUSH, currentLineNumber)
                        super.visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            owner,
                            "logOnce",
                            "(IILjava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;Ljava/lang/String;I)V",
                            false
                        )
                        return
                    }

                    "(IILjava/lang/Throwable;)V" -> {
                        super.visitLdcInsn(simpleClassName)
                        super.visitIntInsn(Opcodes.SIPUSH, currentLineNumber)
                        super.visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            owner,
                            "logOnce",
                            "(IILjava/lang/Throwable;Ljava/lang/String;I)V",
                            false
                        )
                        return
                    }

                    else -> {
                        logger.warn("Unknown descriptor: $descriptor")
                    }
                }
            } else {
                val priority = LogLevel.fromName(name) ?: LogLevel.VERBOSE
                when (descriptor) {
                    "(Ljava/lang/String;[Ljava/lang/Object;)V" -> {
                        super.visitIntInsn(Opcodes.BIPUSH, priority.level)
                        super.visitLdcInsn(simpleClassName)
                        super.visitIntInsn(Opcodes.SIPUSH, currentLineNumber)
                        super.visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            owner,
                            "log",
                            "(Ljava/lang/String;[Ljava/lang/Object;ILjava/lang/String;I)V",
                            false
                        )
                        return
                    }

                    "(Ljava/lang/Throwable;)V" -> {
                        super.visitIntInsn(Opcodes.BIPUSH, priority.level)
                        super.visitLdcInsn(simpleClassName)
                        super.visitIntInsn(Opcodes.SIPUSH, currentLineNumber)
                        super.visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            owner,
                            "log",
                            "(Ljava/lang/Throwable;ILjava/lang/String;I)V",
                            false
                        )
                        return
                    }

                    "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V" -> {
                        super.visitIntInsn(Opcodes.BIPUSH, priority.level)
                        super.visitLdcInsn(simpleClassName)
                        super.visitIntInsn(Opcodes.SIPUSH, currentLineNumber)
                        super.visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            owner,
                            "log",
                            "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;ILjava/lang/String;I)V",
                            false
                        )
                        return
                    }

                    else -> {
                        logger.warn("Unknown descriptor: $descriptor")
                    }
                }
            }
        } else if (owner == ILOGGER_CLASS_NAME && opcode == Opcodes.INVOKEINTERFACE) {
            val priority = LogLevel.fromName(name) ?: LogLevel.VERBOSE

            when (descriptor) {
                "(Ljava/lang/String;[Ljava/lang/Object;)V" -> {
                    iconst(priority.level)
                    iconst(currentLineNumber)
                    super.visitMethodInsn(
                        Opcodes.INVOKEINTERFACE,
                        owner,
                        "log",
                        "(Ljava/lang/String;[Ljava/lang/Object;II)V",
                        true
                    )
                    return
                }

                "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V" -> {
                    iconst(priority.level)
                    iconst(currentLineNumber)
                    super.visitMethodInsn(
                        Opcodes.INVOKEINTERFACE,
                        owner,
                        "log",
                        "(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;II)V",
                        true
                    )
                    return
                }

                "(Ljava/lang/Throwable;)V" -> {
                    iconst(priority.level)
                    iconst(currentLineNumber)
                    super.visitMethodInsn(
                        Opcodes.INVOKEINTERFACE,
                        owner,
                        "log",
                        "(Ljava/lang/Throwable;II)V",
                        true
                    )
                    return
                }

                else -> {
                    logger.warn("Unknown descriptor: $descriptor")
                }
            }
        }

        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }

    private fun iconst(intValue: Int) {
        if (intValue >= -1 && intValue <= 5) {
            mv.visitInsn(Opcodes.ICONST_0 + intValue)
        } else if (intValue >= Byte.MIN_VALUE && intValue <= Byte.MAX_VALUE) {
            mv.visitIntInsn(Opcodes.BIPUSH, intValue)
        } else if (intValue >= Short.MIN_VALUE && intValue <= Short.MAX_VALUE) {
            mv.visitIntInsn(Opcodes.SIPUSH, intValue)
        } else {
            mv.visitLdcInsn(intValue)
        }
    }

    companion object {
        private const val ILOGGER_CLASS_NAME = "it/sephiroth/android/library/asm/trunk/runtime/ILogger"
        private const val TRUNK_CLASS_NAME = "it/sephiroth/android/library/asm/trunk/runtime/Trunk"
    }
}

enum class LogLevel(val level: Int, val tag: String) {
    VERBOSE(2, "v"),
    DEBUG(3, "d"),
    INFO(4, "i"),
    WARN(5, "w"),
    ERROR(6, "e"),
    ASSERT(7, "wtf");

    companion object {
        fun fromName(name: String?): LogLevel? {
            if (name == null) return null
            return entries.firstOrNull { it.tag == name }
        }
    }
}
