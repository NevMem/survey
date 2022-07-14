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
        log.warn(client.toString())

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

        val imports = mutableListOf<String>()

        functions.forEach { function ->
            function.parameters.forEach { param ->
                val paramType = param.type
                val resolved = paramType.resolve()
                imports.add(resolved.declaration.packageName.asString() + "." + resolved.toString())
            }

            function.returnType?.let {
                val type = it.resolve()
                imports.add(type.declaration.packageName.asString() + "." + type.declaration.toString())
            }
        }

        imports.toSet().toList().sorted().forEach {
            printer.println("import $it")
        }
        printer.println()

        printer.println("class $generatedClassName : $client {")

        functions.forEach { function ->
            val params = function.parameters.map { param ->
                "$param: ${param.type}"
            }

            val returnType = function.returnType?.let {
                val type = it.resolve()
                type.toString()
            } ?: "Unit"

            printer.println("\toverride suspend fun $function(${params.joinToString(", ")}): $returnType = TODO(\"Not implemented\")")
        }

        printer.println("}")

        printer.flush()
        printer.close()
    }
}
