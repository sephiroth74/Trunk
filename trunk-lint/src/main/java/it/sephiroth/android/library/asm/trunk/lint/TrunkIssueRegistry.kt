package it.sephiroth.android.library.asm.trunk.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API

class TrunkIssueRegistry : IssueRegistry() {
    override val issues = listOf(LogReplacementDetector.ISSUE)
    override val api = CURRENT_API
    override val minApi = 10 
}
