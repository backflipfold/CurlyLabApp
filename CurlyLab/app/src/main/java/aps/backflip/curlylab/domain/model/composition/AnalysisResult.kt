package aps.backflip.curlylab.domain.model.composition

data class AnalysisIssue(
    val ingredient: String,
    val category: String,
    val reason: String
)

data class AnalysisResult(
    val result: String,
    val issues: List<AnalysisIssue>? = null
)
