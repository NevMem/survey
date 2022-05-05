package com.nevmem.survey.survey

interface MetricsProvider {
    val value: Double
    val name: String
}

internal interface MetricsObserver {
    fun updateValue(value: Double)
}

object AnswersServiceMetrics {
    private val queueSize by lazy {
        object : MetricsProvider, MetricsObserver {
            override var value: Double = 0.0
            override val name: String = "answers_queue_size"

            override fun updateValue(value: Double) {
                this.value = value
            }
        }
    }

    val answersQueueSizeMetrics: MetricsProvider = queueSize
    internal val answersQueueSizeObserver: MetricsObserver = queueSize

    val all: List<MetricsProvider> = listOf(
        answersQueueSizeMetrics,
    )
}
