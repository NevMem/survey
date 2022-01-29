package com.nevmem.survey

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Nullability

data class TsListObject(val objectType: String)

sealed class TsField {
    data class TsInteger(val name: String, val nullable: Boolean = false) : TsField()
    data class TsString(val name: String, val nullable: Boolean = false) : TsField()
    data class TsObject(val name: String, val objectType: String, val nullable: Boolean = false) : TsField()
    data class TsList(val name: String, val fieldType: TsListObject) : TsField()
}

interface TsNode

data class TsClass(
    val parent: TsClass?,
    val name: String,
    val fields: List<TsField>,
) : TsNode

data class TsEnumCase(
    val name: String,
)

data class TsEnum(
    val name: String,
    val cases: List<TsEnumCase>
) : TsNode

class SurveySymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private val nodes = mutableListOf<TsNode>()
    private val serialNames = mutableMapOf<String, String>()

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
            it.accept(
                object : KSVisitorVoid() {
                    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
                        super.visitClassDeclaration(classDeclaration, data)
                        if (classDeclaration.getSealedSubclasses().toList().isNotEmpty()) {
                            acceptSealedClass(classDeclaration)
                        } else if (classDeclaration.classKind == ClassKind.ENUM_CLASS) {
                            logger.warn("Found enum class $classDeclaration")
                            acceptEnumClass(classDeclaration)
                        } else {
                            acceptClass(classDeclaration)
                        }
                    }
                },
                Unit
            )
            logger.warn("done processing $it")
        }

        resolver.getSymbolsWithAnnotation("kotlinx.serialization.SerialName").forEach {
            it.accept(
                object : KSVisitorVoid() {
                    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
                        super.visitClassDeclaration(classDeclaration, data)
                        logger.warn("Found class with SerialName annotation $classDeclaration")
                        classDeclaration.annotations.filter {
                            it.annotationType.resolve().toString() == "SerialName"
                        }.forEach {
                            it.arguments.find {
                                it.toString().startsWith("value:")
                            }?.let {
                                logger.warn("Serial name: ${(it.value as? String)?.replace("value:", "")}")
                                serialNames[classDeclaration.toString()] = (it.value as? String)?.replace("value:", "") ?: ""
                            }
                        }
                    }
                },
                Unit,
            )
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
                val writeNode: (TsClass) -> Unit = { node ->
                    if (node.parent == null) {
                        write("interface ${node.name} {\n")
                    } else {
                        write("interface ${node.name} extends ${node.parent.name} {\n")
                    }
                    node.fields.forEach { field ->
                        when (field) {
                            is TsField.TsInteger -> {
                                val nl = " | undefined".takeIf { field.nullable } ?: ""
                                write("\t${field.name}: number$nl;\n")
                            }
                            is TsField.TsString -> {
                                val nl = " | undefined".takeIf { field.nullable } ?: ""
                                write("\t${field.name}: string$nl;\n")
                            }
                            is TsField.TsObject -> {
                                val nl = " | undefined".takeIf { field.nullable } ?: ""
                                write("\t${field.name}: ${field.objectType}$nl;\n")
                            }
                            is TsField.TsList -> {
                                write("\t${field.name}: ${field.fieldType.objectType}[];\n")
                            }
                            else -> {}
                        }
                    }
                    write("}\n\n")
                }

                nodes.filterIsInstance<TsClass>().forEach(writeNode)

                nodes.filterIsInstance<TsClass>().filter { it.parent != null }.forEach { node ->
                    // Writing instanceOf helpers

                    if (serialNames.containsKey(node.name)) {
                        write("export function instanceOf${node.name}(object: ${node.parent!!.name}): object is ${node.name} {\n")
                        write("\treturn object.type === \"${serialNames[node.name]}\";\n")
                        write("}\n\n")
                    }
                }

                nodes.filterIsInstance<TsEnum>().forEach {
                    write("enum ${it.name} {\n")
                    write(
                        it.cases.map { case -> case.name + " = \"${case.name}\"" }
                            .joinToString(",\n") { "    $it" }
                    )
                    write("\n}\n\n")
                }

                write("export type {\n")
                nodes
                    .map {
                        when (it) {
                            is TsClass -> it.name
                            is TsEnum -> it.name
                            else -> throw IllegalStateException()
                        }
                    }
                    .sorted()
                    .forEach {
                        write("\t$it,\n")
                    }
                write("}\n")

                close()
            }

        return emptyList()
    }

    private fun acceptSealedClass(decl: KSClassDeclaration) {
        logger.warn("Found sealed class $decl")
        val node = TsClass(null, decl.toString(), listOf(TsField.TsString("type")))
        decl.getSealedSubclasses().forEach {
            acceptClass(it, node)
        }
        nodes.add(node)
    }

    private fun acceptEnumClass(decl: KSClassDeclaration) {
        check(decl.classKind == ClassKind.ENUM_CLASS) { "Something went wrong" }

        val cases = mutableListOf<TsEnumCase>()

        decl.declarations.filterIsInstance<KSClassDeclaration>().forEach {
            cases.add(TsEnumCase(it.simpleName.asString()))
        }

        nodes.add(TsEnum(decl.simpleName.asString(), cases))
    }

    private fun acceptClass(decl: KSClassDeclaration, parent: TsClass? = null) {
        logger.warn("Found usual class $decl")
        val fields = mutableListOf<TsField>()
        decl.getAllProperties().forEach {
            val fieldName = it.toString()
            when (val declaration = it.type.resolve().declaration.toString()) {
                "List" -> {
                    val argument = it.type.resolve().arguments.first().type.toString()
                    fields.add(TsField.TsList(fieldName, TsListObject(argument)))
                }
                "Int", "Long" -> {
                    fields.add(TsField.TsInteger(fieldName, it.type.resolve().nullability == Nullability.NULLABLE))
                }
                "String" -> {
                    fields.add(TsField.TsString(fieldName, it.type.resolve().nullability == Nullability.NULLABLE))
                }
                else -> {
                    fields.add(TsField.TsObject(fieldName, declaration, it.type.resolve().nullability == Nullability.NULLABLE))
                }
            }
        }
        val node = TsClass(parent, decl.toString(), fields)
        nodes.add(node)
    }
}
