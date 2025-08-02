package myanalogcodegenerator.parser

import domain.model.*
import domain.repository.ArchitectureDatabase
import org.treesitter.*
import java.io.File

class KotlinTreeSitterRepository(
    private val folder: File
) {
    private val parser = TSParser().apply {
        language = TreeSitterKotlin()
    }

    fun parseFolderToArchitectureDatabase(
    ): ArchitectureDatabase {
        require(folder.exists() && folder.isDirectory) {
            "Provided path must be an existing directory"
        }

        var architectureDatabase = ArchitectureDatabase()

        folder.walkTopDown()
            .filter { it.isFile }
            .forEach { file ->
                architectureDatabase = architectureDatabase.addNode(
                    parseFile(file)
                )
            }

        return architectureDatabase
    }

    fun parseFile(file: File): ArchitectureNode {
        val source = file.readText()
        val tree = parser.parseString(null, source)
        val className = findClassName(tree, source)
        val methods = findMethods(tree, source)
        val attributes = findAttributes(tree, source)
        val dependencies = findDependencies(tree, source)

        return ArchitectureNode(
            id = className,
            name = className,
            layer = file.path.toLayerValue(),
            methods = methods,
            attributes = attributes,
            dependencies = dependencies
        )
    }

    fun String.toLayerValue(): ArchitectureLayer {
        when {
            this.contains("domain") -> {
                return ArchitectureLayer.DOMAIN
            }

            this.contains("data") -> {
                return ArchitectureLayer.DATA
            }

            this.contains("presentation") -> {
                return ArchitectureLayer.PRESENTATION
            }

            else -> {
                return ArchitectureLayer.OTHER
            }
        }
    }

    /**
     * Find class name
     */
    private fun findClassName(tree: TSTree, source: String): String {
        val query = TSQuery(
            TreeSitterKotlin(), """
        (class_declaration (type_identifier) @class_name)
    """.trimIndent()
        )

        val cursor = TSQueryCursor()
        cursor.exec(query, tree.rootNode)

        for (match in cursor.matches) {
            for (cap in match.captures) {
                return extractTextByByteRange(source, cap.node.startByte, cap.node.endByte)
            }
        }
        return "UnnamedClass"
    }

    private fun findMethods(tree: TSTree, source: String): List<NodeMethod> {
        val query = TSQuery(TreeSitterKotlin(), """
        (
          (function_declaration
            (modifiers (_) @modifier)?
            (simple_identifier) @func_name
            (function_value_parameters
              (parameter
                (simple_identifier) @param_name
                (user_type (type_identifier) @param_type)
              )+
            )?
            (user_type (type_identifier) @return_type)?
          )
        )
        """.trimIndent())

        val cursor = TSQueryCursor()
        cursor.exec(query, tree.rootNode)

        val methods = mutableListOf<NodeMethod>()

        for (match in cursor.matches) {
            var funcName = ""
            var returnType = "Unit"
            var rawParams = ""

            for (cap in match.captures) {
                val capture = query.getCaptureNameForId(cap.index)
                val text = extractTextByByteRange(source, cap.node.startByte, cap.node.endByte)

                when (capture) {
                    "func_name" -> funcName = text
                    "return_type" -> returnType = text
                    "params" -> rawParams = text // will be like "(id: Int, name: String)"
                }
            }

            val paramList = rawParams
                .removePrefix("(").removeSuffix(")")
                .split(',')
                .mapNotNull { part ->
                    val pieces = part.trim().split(":")
                    if (pieces.size == 2) {
                        val name = pieces[0].trim()
                        val type = pieces[1].trim()
                        NodeParameter(name, type)
                    } else null
                }
            val nodeMethod = NodeMethod(
                name = funcName,
                returnType = returnType,
                parameters = paramList
            )
            //todo: Write a better query, this is added because same abstract functions
            // are recognised twice or so...
            if (!methods.contains(nodeMethod)) {
                methods += nodeMethod
            }
        }

        return methods
    }

    fun findAttributes(tree: TSTree, source: String): List<NodeAttribute> {
        val query = TSQuery(
            TreeSitterKotlin(), """
        (property_declaration
          (modifiers (visibility_modifier)? (member_modifier)?)?
          (binding_pattern_kind) @mutability
          (variable_declaration
            (simple_identifier) @field_name
            (_)? @field_type)?) ; match even if no type declared
        """.trimIndent()
        )

        val cursor = TSQueryCursor()
        cursor.exec(query, tree.rootNode)

        val attributes = mutableListOf<NodeAttribute>()

        for (match in cursor.matches) {
            var name = ""
            var type = "Any"
            var isMutable = false

            for (cap in match.captures) {
                val captureName = query.getCaptureNameForId(cap.index)
                val text = extractTextByByteRange(source, cap.node.startByte, cap.node.endByte)

                when (captureName) {
                    "field_name" -> name = text
                    "field_type" -> if (text != "=") type = text // avoid "=" being interpreted as type
                    "mutability" -> isMutable = text.trim() == "var"
                }
            }

            if (name.isNotBlank()) {
                attributes += NodeAttribute(
                    name = name,
                    type = type,
                    isMutable = isMutable
                )
            }
        }

        return attributes
    }

    fun findDependencies(tree: TSTree, source: String): List<NodeDependency> {
        val query = TSQuery(
            TreeSitterKotlin(), """
        (property_declaration
          (variable_declaration
            (simple_identifier) @dep_name
            (user_type (type_identifier) @dep_type)
          )
        )
        """.trimIndent()
        )

        val cursor = TSQueryCursor()
        cursor.exec(query, tree.rootNode)

        val dependencies = mutableListOf<NodeDependency>()

        for (match in cursor.matches) {
            var name = ""
            var type = ""

            for (cap in match.captures) {
                val captureName = query.getCaptureNameForId(cap.index)
                val text = extractTextByByteRange(source, cap.node.startByte, cap.node.endByte)

                when (captureName) {
                    "dep_name" -> name = text
                    "dep_type" -> type = text
                }
            }

            if (type.isNotBlank()) {
                dependencies += NodeDependency(
                    targetId = type,
                    type = DependencyType.USES
                )
            }
        }

        return dependencies
    }

    private fun extractTextByByteRange(source: String, startByte: Int, endByte: Int): String {
        val bytes = source.toByteArray(Charsets.UTF_8)
        val slicedBytes = bytes.sliceArray(startByte until endByte)
        return slicedBytes.toString(Charsets.UTF_8)
    }

}