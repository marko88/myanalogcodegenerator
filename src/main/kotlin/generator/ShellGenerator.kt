package myanalogcodegenerator.generator

import com.squareup.kotlinpoet.*
import domain.model.*
import domain.repository.ArchitectureDatabase
import java.nio.file.Path

object ShellGenerator {

    fun generate(db: ArchitectureDatabase, outDir: Path, packageBase: String = "gen") {
        db.getAllNodes().forEach { node ->
            val pkg = "$packageBase.${node.layer.name.lowercase()}"
            val file = when (node.type) {
                ArchitectureNodeType.REPOSITORY,
                ArchitectureNodeType.API,
                ArchitectureNodeType.VIEWMODEL -> buildInterface(node, pkg)

                else -> buildAbstract(node, pkg)
            }
            file.writeTo(outDir)
        }
    }

    /* interfaces --------------------------------------------------- */
    private fun buildInterface(node: ArchitectureNode, pkg: String): FileSpec {
        val type = TypeSpec.interfaceBuilder("${node.name}Gen").apply {
            // Add abstract dependency references
            node.dependencies.forEach { dep ->
                val className = ClassName(pkg.substringBeforeLast("."), "${dep.targetId}Gen")
                val propName = dep.targetId.replaceFirstChar { it.lowercaseChar() }

                addProperty(
                    PropertySpec.builder(propName, className)
                        .addModifiers(KModifier.ABSTRACT)
                        .build()
                )
            }

            // Add other methods and attributes
            node.methods.forEach { addFunction(it.toFunSpec(abstract = false)) }
            node.attributes.forEach { addProperty(it.toPropertySpec()) }
        }.build()

        return FileSpec.builder(pkg, type.name!!)
            .addFileComment(AUTOGEN)
            .addType(type)
            .build()
    }

    /* abstract classes --------------------------------------------- */
    private fun buildAbstract(node: ArchitectureNode, pkg: String): FileSpec {
        val cls = TypeSpec.classBuilder("${node.name}Gen")
            .addModifiers(KModifier.ABSTRACT)
            .apply {
                // Generate dependencies as constructor parameters and properties
                node.dependencies.forEach { dep ->
                    val className = ClassName(pkg.substringBeforeLast("."), "${dep.targetId}Gen")
                    val paramName = dep.targetId.replaceFirstChar { it.lowercaseChar() }

                    primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter(paramName, className)
                            .build()
                    )

                    addProperty(
                        PropertySpec.builder(paramName, className)
                            .initializer(paramName)
                            .addModifiers(KModifier.PROTECTED)
                            .build()
                    )
                }

                node.attributes.forEach { addProperty(it.toPropertySpec()) }
                node.methods.forEach { addFunction(it.toFunSpec(abstract = true)) }
            }
            .build()

        return FileSpec.builder(pkg, cls.name!!)
            .addFileComment(AUTOGEN)
            .addType(cls)
            .build()
    }

    private const val AUTOGEN =
        "AUTO-GENERATED by ArchDesigner – Do NOT edit, your changes will be lost."
}