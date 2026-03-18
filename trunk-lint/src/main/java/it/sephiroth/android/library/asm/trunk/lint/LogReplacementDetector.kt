package it.sephiroth.android.library.asm.trunk.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression

class LogReplacementDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> = listOf("v", "d", "i", "w", "e", "wtf")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val evaluator = context.evaluator
        val isLog = evaluator.isMemberInClass(method, "android.util.Log")
        val isTimber = evaluator.isMemberInClass(method, "timber.log.Timber") ||
                evaluator.isMemberInClass(method, "timber.log.Timber.Tree")

        if (isLog || isTimber) {
            reportIssue(context, node, if (isLog) "Log" else "Timber")
        }
    }

    private fun reportIssue(context: JavaContext, node: UCallExpression, type: String) {
        val methodName = node.methodName ?: return
        val arguments = node.valueArguments
        
        val fixBuilder = fix().replace()
            .range(context.getLocation(node))

        val newCall = if (type == "Log") {
            // Log.d(TAG, message, throwable) -> Trunk.d(message, throwable)
            // Remove (TAG)
            if (arguments.size >= 2) {
                val remainingArgs = arguments.drop(1).joinToString(", ") { it.asSourceString() }
                "Trunk.$methodName($remainingArgs)"
            } else {
                "Trunk.$methodName(${arguments.joinToString(", ") { it.asSourceString() }})"
            }
        } else {
            // Timber.d(message, args) -> Trunk.d(message, args)
            // Timber has the same signature of Trunk (senza TAG obbligatorio)
            "Trunk.$methodName(${arguments.joinToString(", ") { it.asSourceString() }})"
        }

        val fix = fixBuilder
            .with(newCall)
            .shortenNames()
            .reformat(true)
            .build()

        context.report(
            ISSUE, node, context.getLocation(node),
            "Use Trunk instead of $type",
            fix
        )
    }

    companion object {
        val ISSUE = Issue.create(
            id = "UseTrunkLogger",
            briefDescription = "Replaces the standard Log class with Trunk",
            explanation = "Trunk automatically manages tag and line number through ASM, making log messages more efficient.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(LogReplacementDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }
}
