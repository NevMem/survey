package com.nevmem.survey.util.client

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.nevmem.survey.util.client.internal.HttpClientGeneratorSymbolProcessor

class HttpClientGeneratorKspProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = HttpClientGeneratorSymbolProcessor(environment)
}
