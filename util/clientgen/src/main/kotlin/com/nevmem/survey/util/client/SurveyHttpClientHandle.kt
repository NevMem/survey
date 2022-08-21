package com.nevmem.survey.util.client

enum class RetryPolicy {
    None,
    Exponential,
    ExponentialFinite,
    Linear,
    LinearFinite,
}

@Target(AnnotationTarget.FUNCTION)
annotation class SurveyHttpClientHandle(
    val path: String,
    val retryPolicy: RetryPolicy = RetryPolicy.None,
)
