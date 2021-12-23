package com.nevmem.survey

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyAccessor
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.File

sealed class TsField {
    data class TsInteger(val name: String) : TsField()
    data class TsString(val name: String) : TsField()
    data class TsObject(val name: String, val objectType: String) : TsField()
    data class TsListObject(val objectType: String) : TsField()
    data class TsList(val name: String, val fieldType: TsField.TsListObject) : TsField()
}

data class TsNode(
    val parent: TsNode?,
    val name: String,
    val fields: List<TsField>,
)

class SurveySymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private val nodes = mutableListOf<TsNode>()

    private var counter = 0

    init {
        logger.warn("SurveySymbolProcessor init")
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        counter += 1
        if (counter >= 2) {
            return emptyList()
        }

        resolver.getSymbolsWithAnnotation("com.nevmem.survey.Exported").forEach {
            logger.warn("processing $it")
            it.accept(object : KSVisitorVoid() {
                override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
                    super.visitClassDeclaration(classDeclaration, data)
                    if (classDeclaration.getSealedSubclasses().toList().isNotEmpty()) {
                        acceptSealedClass(classDeclaration)
                    } else {
                        acceptClass(classDeclaration)
                    }
                }
            }, Unit)
            logger.warn("done processing $it")
        }

        codeGenerator.createNewFile(
            Dependencies(
                false,
                *resolver.getAllFiles().toList().toTypedArray()
            ),
            "com.nevmem.survey",
            "exported",
            "ts"
        )
        .writer()
        .apply {
            val writeNode: (TsNode) -> Unit = { node ->
                if (node.parent == null) {
                    write("interface ${node.name} {\n")
                } else {
                    write("interface ${node.name} extends ${node.parent.name} {\n")
                }
                node.fields.forEach {
                    when (it) {
                        is TsField.TsInteger -> {
                            write("\t${it.name}: number;\n")
                        }
                        is TsField.TsString -> {
                            write("\t${it.name}: string;\n")
                        }
                        is TsField.TsObject -> {
                            write("\t${it.name}: ${it.objectType};\n")
                        }
                        is TsField.TsList -> {
                            write("\t${it.name}: ${it.fieldType.objectType}[];\n")
                        }
                        else -> {}
                    }
                }
                write("}\n\n")
            }

            nodes.filter { it.parent == null }.forEach(writeNode)
            nodes.filter { it.parent != null }.forEach(writeNode)
            close()
        }

        nodes.forEach {
            logger.warn("$it")
        }

        return emptyList()
    }

    private fun acceptSealedClass(decl: KSClassDeclaration) {
        logger.warn("Found sealed class $decl")
        val node = TsNode(null, decl.toString(), emptyList())
        decl.getSealedSubclasses().forEach {
            acceptClass(it, node)
        }
        nodes.add(node)
    }

    private fun acceptClass(decl: KSClassDeclaration, parent: TsNode? = null) {
        logger.warn("Found usual class $decl")
        val fields = mutableListOf<TsField>()
        decl.getAllProperties().forEach {
            logger.warn("$it")
            val fieldName = it.toString()
            val declaration = it.type.resolve().declaration.toString()
            logger.warn("Found declaration $declaration")
            when (declaration) {
                "List" -> {
                    val argument = it.type.resolve().arguments.first().type.toString()
                    logger.warn("Found list with argument type $argument")
                    fields.add(TsField.TsList(fieldName, TsField.TsListObject(argument)))
                }
                "Int", "Long" -> {
                    fields.add(TsField.TsInteger(fieldName))
                }
                "String" -> {
                    fields.add(TsField.TsString(fieldName))
                }
                else -> {
                    throw IllegalStateException("Declaration $declaration is not supported")
                }
            }
        }
        val node = TsNode(parent, decl.toString(), fields)
        nodes.add(node)
    }
}