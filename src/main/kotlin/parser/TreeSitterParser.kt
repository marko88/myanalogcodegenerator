package myanalogcodegenerator.parser

import domain.model.*
import org.treesitter.*

class TreeSitterParser() {

    fun parse() {
        /* ----------  set-up parser ---------- */

        val parser = TSParser()
        parser.setLanguage(TreeSitterKotlin())             // loads native dylib/so/dll

        /* ----------  sample source ---------- */

        val source = """
    package demo
    
    public class Main() {

        fun greet(name: String) {
            println("Hello, " + name)
        }
        
        fun test(name: String) {
            println("Hello, " + name)
        }
        
        fun test2(name: String) {
            println("Hello, " + name)
        }
    }
""".trimIndent()

        parser.setLanguage(TreeSitterKotlin())
        val tree: TSTree = parser.parseString(null, source)
        println(tree.rootNode) // sanity-check

//        val query = TSQuery(TreeSitterKotlin(), """
//            (function_declaration
//                (simple_identifier) @func_name)
//        """.trimIndent())

        val query = TSQuery(TreeSitterKotlin(), """
            (class_declaration
                (type_identifier) @class_name)
        """.trimIndent())

        val cursor = TSQueryCursor()
        cursor.exec(query, tree.rootNode)

        for (match in cursor.matches) {
            for (cap in match.captures) {
                val captureName = query.getCaptureNameForId(cap.index)
                val text = source.substring(cap.node.startByte, cap.node.endByte)
                println("Capture '$captureName' -> $text")
            }
        }
    }
}