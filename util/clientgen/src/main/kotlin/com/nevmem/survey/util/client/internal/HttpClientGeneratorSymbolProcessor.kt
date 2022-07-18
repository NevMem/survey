package com.nevmem.survey.util.client.internal

import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.nevmem.survey.util.client.RetryPolicy
import com.nevmem.survey.util.client.SurveyHttpClient
import com.nevmem.survey.util.client.SurveyHttpClientHandle
import java.io.PrintWriter

internal class HttpClientGeneratorSymbolProcessor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    private val log by lazy { environment.logger }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(SurveyHttpClient::class.java.name, inDepth = true).forEach {
            processClient(it)
        }

        return emptyList()
    }

    private fun processClient(client: KSAnnotated) {
        val functions = mutableListOf<KSFunctionDeclaration>()

        val visitor = object : KSVisitorVoid() {
            override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
                classDeclaration.getAllFunctions().forEach {
                    if (it.annotations.any { it.shortName.asString() == SurveyHttpClientHandle::class.java.simpleName }) {
                        functions.add(it)
                    }
                }
            }
        }

        client.accept(
            visitor,
            Unit,
        )

        log.warn("Found client $client with ${functions.size} handle methods")

        val packageName = client.containingFile!!.packageName.asString()
        val generatedClassName = "Generated$client"

        val stream = environment.codeGenerator.createNewFile(
            dependencies = Dependencies(true, client.containingFile!!),
            packageName = packageName,
            fileName = generatedClassName,
            extensionName = "kt",
        )
        val printer = PrintWriter(stream)

        printer.println("package $packageName")
        printer.println()

        printer.declareImports(functions)

        printer.println("internal class $generatedClassName(private val baseUrl: String) : $client {")

        printer.declareClient()
        functions.forEach { function ->
            printer.declareHandle(function)
        }
        printer.declarePostFunction()

        printer.declareRetryFunctions()

        printer.println("}")

        printer.flush()
        printer.close()
    }

    private fun createImports(functions: List<KSFunctionDeclaration>): List<String> {
        val imports = mutableListOf(
            "io.ktor.client.HttpClient",
            "io.ktor.client.features.json.JsonFeature",
            "io.ktor.client.features.json.serializer.KotlinxSerializer",
            "io.ktor.client.features.logging.DEFAULT",
            "io.ktor.client.features.logging.LogLevel",
            "io.ktor.client.features.logging.Logger",
            "io.ktor.client.features.logging.Logging",
            "io.ktor.client.request.post",
            "io.ktor.http.ContentType",
            "io.ktor.http.contentType",
            "kotlinx.coroutines.delay",
        )

        functions.forEach { function ->
            function.parameters.forEach { param ->
                val paramType = param.type
                val resolved = paramType.resolve()
                imports.add(resolved.declaration.packageName.asString() + "." + resolved.toString())
            }

            function.returnType?.let {
                val type = it.resolve()
                imports.add(type.declaration.packageName.asString() + "." + type.declaration.toString())
                val args = type.arguments
                args.forEach {
                    imports.add(it.type!!.resolve().declaration.packageName.asString() + "." + it.type.toString())
                }
            }
        }

        return imports.toSet().toList().sorted()
    }

    private fun PrintWriter.declareImports(functions: List<KSFunctionDeclaration>) {
        val imports = createImports(functions)
        imports.forEach {
            println("import $it")
        }
        println()
    }

    private fun PrintWriter.declareHandle(function: KSFunctionDeclaration) {
        assert(function.parameters.size <= 1)

        val paramsCount = function.parameters.size

        val paramName = function.parameters.map { param -> "$param" }.firstOrNull()
        val paramString = function.parameters.map { param -> "$param: ${param.type}" }.firstOrNull()

        val returnType = function.returnType?.let {
            val type = it.resolve()
            type.toString()
        } ?: "Unit"

        val annotation = function.annotations.find { it.shortName.asString() == SurveyHttpClientHandle::class.java.simpleName }!!

        val path = annotation.arguments.find { it.name?.asString() == "path" }!!.value.toString()

        val withRetryPolicy: (String) -> String = { call: String ->
            val retryPolicy = RetryPolicy.valueOf(
                annotation.arguments.find { it.name?.asString() == "retryPolicy" }!!.value.toString()
                    .split(".")
                    .last()
            )
            log.warn(retryPolicy.toString())
            when (retryPolicy) {
                RetryPolicy.None -> call
                RetryPolicy.Exponential -> "exponentialRetry { $call }"
                RetryPolicy.ExponentialFinite -> "exponentialRetryFinite { $call }"
                RetryPolicy.Linear -> TODO()
                RetryPolicy.LinearFinite -> TODO()
            }
        }

        if (paramsCount == 1) {
            println("\toverride suspend fun $function($paramString): $returnType = ${withRetryPolicy("post(\"$path\", $paramName)")}")
        } else {
            println("\toverride suspend fun $function(): $returnType = ${withRetryPolicy("post(\"$path\", Unit)")}")
        }
    }

    private fun PrintWriter.declareClient() {
        println()
        println(
            """
            private val client = HttpClient {
                install(JsonFeature) { serializer = KotlinxSerializer() }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.BODY
                }
            }
            """.replaceIndent("    ")
        )
        println()
    }

    private fun PrintWriter.declarePostFunction() {
        println()
        println(
            """
                private suspend inline fun <Req : Any, reified Res : Any> post(path: String, body: Req): Res {
                    return client.post(baseUrl + path) {
                        this.body = body
                        contentType(ContentType.Application.Json)
                    }
                }
            """.replaceIndent("    ")
        )
        println()
    }

    /* suspend fun delay(time: Long) = Unit

    class RequestFailedException(message: String) : Exception(message)
    suspend fun <T> exponentialRetryFinite(doRequest: () -> T): T {
        var delayTime = 1000L
        var requestCount = 0
        while (true) {
            requestCount += 1
            try {
                return doRequest()
            } catch (exception: Exception) {
                if (requestCount == 3) {
                    throw RequestFailedException(exception.message ?: "Retry failed")
                }

                delay(delayTime)
                delayTime *= 2
                delayTime = delayTime.coerceAtMost(60 * 1000L)
            }
        }
    } */

    private fun PrintWriter.declareRetryFunctions() {
        println(
            """
            class RequestFailedException(message: String) : Exception(message)
            
            private suspend fun <T> exponentialRetry(doRequest: suspend () -> T): T {
                var delayTime = 1000L
                while (true) {
                    try {
                        return doRequest()
                    } catch (_: Exception) {
                        delay(delayTime)
                        delayTime *= 2
                        delayTime = delayTime.coerceAtMost(60 * 1000L)
                    }
                }
            }
            
            private suspend fun <T> exponentialRetryFinite(doRequest: suspend () -> T): T {
                var delayTime = 1000L
                var requestCount = 0
                while (true) {
                    requestCount += 1
                    try {
                        return doRequest()
                    } catch (exception: Exception) {
                        if (requestCount == 3) {
                            throw RequestFailedException(exception.message ?: "Retry failed")
                        }
                        
                        delay(delayTime)
                        delayTime *= 2
                        delayTime = delayTime.coerceAtMost(60 * 1000L)
                    }
                }
            }
            """.replaceIndent("    ")
        )
    }
}
