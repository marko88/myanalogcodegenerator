package myanalogcodegenerator.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy


fun String.kotlinPoet(): TypeName {
    // Simple built-ins -------------------------------------------------
    return when (this.trim()) {
        "Unit"    -> UNIT
        "Int"     -> INT
        "Long"    -> LONG
        "Float"   -> FLOAT
        "Double"  -> DOUBLE
        "Boolean" -> BOOLEAN
        "String"  -> STRING
        // Generic? parse it -------------------------------------------
        else -> {
            if ('<' in this && this.endsWith('>')) {
                parseGeneric(this)
            } else {
                ClassName.bestGuess(this)
            }
        }
    }
}

/* ───────────────────── helper for "List<Foo>" etc. ────────────────── */
private fun parseGeneric(text: String): TypeName {
    // very light-weight split, assumes no nested generics like Map<A,B>
    val outer = text.substringBefore('<').trim()
    val inner = text.substringAfter('<').substringBeforeLast('>').trim()

    val outerCN = when (outer) {
        "List" -> ClassName("kotlin.collections", "List")
        "MutableList" -> ClassName("kotlin.collections", "MutableList")
        "Set"  -> ClassName("kotlin.collections", "Set")
        "Flow" -> ClassName("kotlinx.coroutines.flow", "Flow")
        else   -> ClassName.bestGuess(outer)
    }

    val innerTN = inner.kotlinPoet()
    return outerCN.parameterizedBy(innerTN)
}

/* TypeName passthrough so `.kotlinPoet()` is always callable */
fun TypeName.kotlinPoet(): TypeName = this