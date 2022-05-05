package com.nevmem.survey.metrics

import com.nevmem.survey.survey.AnswersServiceMetrics
import com.nevmem.survey.survey.MetricsProvider
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder

class CustomMeterBinder : MeterBinder {
    override fun bindTo(registry: MeterRegistry) {
        AnswersServiceMetrics.all.forEach {
            registry.gauge(it.name, emptyList(), it, MetricsProvider::value)
        }
    }
}
