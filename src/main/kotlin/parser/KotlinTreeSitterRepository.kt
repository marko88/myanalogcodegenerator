package myanalogcodegenerator.parser

import domain.model.ArchitectureLayer
import domain.model.ArchitectureNode
import domain.model.ArchitectureNodeType
import domain.model.NodeMethod
import org.treesitter.*
import java.io.File

class KotlinTreeSitterRepository {
    private val parser = TSParser().apply {
        language = TreeSitterKotlin()
    }


    fun parseFile(file: File, layer: ArchitectureLayer?): ArchitectureNode? {
        val source = file.readText()
        val tree = parser.parseString(null, source)

        val className = findClassName(tree, source)
        val methodNames = findFunctionNames(tree, source)

        return null
    }

    /**
     * Find class name
     */
    private fun findClassName(tree: TSTree, source: String): String {
        val query = TSQuery(TreeSitterKotlin(), """
        (class_declaration (type_identifier) @class_name)
    """.trimIndent())

        val cursor = TSQueryCursor()
        cursor.exec(query, tree.rootNode)

        for (match in cursor.matches) {
            for (cap in match.captures) {
                return extractTextByByteRange(source, cap.node.startByte, cap.node.endByte)
            }
        }
        return "UnnamedClass"
    }

    /**
     * Find function names.
     */
    private fun findFunctionNames(tree: TSTree, source: String): List<String> {
        val query = TSQuery(
            TreeSitterKotlin(), """
            (function_declaration (simple_identifier) @func_name)
        """.trimIndent()
        )
        val cursor = TSQueryCursor()
        cursor.exec(query, tree.rootNode)

        return cursor.matches.asSequence().flatMap { match ->
            match.captures.map { cap ->
                extractTextByByteRange(source, cap.node.startByte, cap.node.endByte)
            }
        }.toList()
    }

    private fun extractTextByByteRange(source: String, startByte: Int, endByte: Int): String {
        val bytes = source.toByteArray(Charsets.UTF_8)
        val slicedBytes = bytes.sliceArray(startByte until endByte)
        return slicedBytes.toString(Charsets.UTF_8)
    }

}