package myanalogcodegenerator.parser

import domain.model.*
import org.treesitter.*
import java.io.File

class KotlinTreeSitterRepository {
    private val parser = TSParser().apply {
        language = TreeSitterKotlin()
    }


    fun parseFile(file: File, layer: ArchitectureLayer?): ArchitectureNode? {
        val source = file.readText()
        val tree = parser.parseString(null, source)
        println(tree.rootNode)
        val className = findMethods(tree, source)
        return null
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
        val query = TSQuery(
            TreeSitterKotlin(), """
            (function_declaration
                (simple_identifier) @func_name
                (function_value_parameters) @params
                (user_type) @return_type)
            """.trimIndent()
        )

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

            methods += NodeMethod(
                name = funcName,
                returnType = returnType,
                parameters = paramList
            )
        }

        return methods
    }

    private fun extractTextByByteRange(source: String, startByte: Int, endByte: Int): String {
        val bytes = source.toByteArray(Charsets.UTF_8)
        val slicedBytes = bytes.sliceArray(startByte until endByte)
        return slicedBytes.toString(Charsets.UTF_8)
    }

}