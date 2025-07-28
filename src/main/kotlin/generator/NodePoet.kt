package myanalogcodegenerator.generator

import com.squareup.kotlinpoet.*
import domain.model.*

/* ---- attributes -> PropertySpec ---- */
fun NodeAttribute.toPropertySpec(): PropertySpec = PropertySpec
    .builder(name, type.kotlinPoet())
    .mutable(isMutable)
    .addKdoc(description)
    .build()

/* ---- methods -> FunSpec ---- */
fun NodeMethod.toFunSpec(abstract: Boolean): FunSpec = FunSpec.builder(name).apply {
    if (isSuspend) addModifiers(KModifier.SUSPEND)
    if (abstract)  addModifiers(KModifier.ABSTRACT)

    parameters.forEach { param ->
        addParameter(param.name, param.type.kotlinPoet())
    }

    returns(returnType.kotlinPoet())
    if (description.isNotBlank()) addKdoc(description)
}.build()