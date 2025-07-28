package myanalogcodegenerator.domain.dsl

import androidx.compose.ui.geometry.Offset
import domain.model.*
import domain.repository.ArchitectureDatabase

@DslMarker annotation class ArchDsl

/*──────────────────────────── ARCHITECTURE ───────────────────────────*/

@ArchDsl
class ArchitectureScope(private val archName: String) {
    private val nodes     = mutableListOf<ArchitectureNode>()
    private val dataFlows = mutableListOf<DataFlowConnection>()

    /* LAYERS */
    fun layer(layer: ArchitectureLayer, build: LayerScope.() -> Unit) =
        LayerScope(layer).apply(build).nodes.forEach(nodes::add)

    fun layer(name: String, build: LayerScope.() -> Unit) =
        layer(ArchitectureLayer.valueOf(name.uppercase()), build)

    /* CROSS-LAYER FLOWS */
    fun flow(build: FlowScope.() -> Unit) {
        FlowScope().apply(build).flows.forEach(dataFlows::add)
    }

    /* BUILD IMMUTABLE DB */
    fun build(): ArchitectureDatabase =
        ArchitectureDatabase().let { db0 ->
            val dbWithNodes = nodes.fold(db0) { d, n -> d.addNode(n) }
            dataFlows.fold(dbWithNodes) { d, f -> d.addDataFlow(f) }
        }
}

/*──────────────────────────── LAYER ─────────────────────────────────*/

@ArchDsl
class LayerScope internal constructor(private val layer: ArchitectureLayer) {
    internal val nodes = mutableListOf<ArchitectureNode>()

    /* convenience wrappers */
    fun view       (id: String, build: ComponentScope.() -> Unit = {}) = component(id, ArchitectureNodeType.VIEW,       build)
    fun presenter  (id: String, build: ComponentScope.() -> Unit = {}) = component(id, ArchitectureNodeType.PRESENTER,  build)
    fun viewModel  (id: String, build: ComponentScope.() -> Unit = {}) = component(id, ArchitectureNodeType.VIEWMODEL,  build)
    fun useCase    (id: String, build: ComponentScope.() -> Unit = {}) = component(id, ArchitectureNodeType.USE_CASE,   build)
    fun repository (id: String, build: ComponentScope.() -> Unit = {}) = component(id, ArchitectureNodeType.REPOSITORY, build)
    fun database   (id: String, build: ComponentScope.() -> Unit = {}) = component(id, ArchitectureNodeType.DATABASE,   build)
    fun api        (id: String, build: ComponentScope.() -> Unit = {}) = component(id, ArchitectureNodeType.API,        build)
    fun component  (id: String, type: ArchitectureNodeType, build: ComponentScope.() -> Unit = {}) =
        nodes.add(ComponentScope(id, type, layer).apply(build).toNode())

    /* COMPONENT SCOPE */
    @ArchDsl
    class ComponentScope(
        private val id: String,
        private val type: ArchitectureNodeType,
        private val layer: ArchitectureLayer
    ) {
        private val attrs   = mutableListOf<NodeAttribute>()
        private val methods = mutableListOf<NodeMethod>()
        private val deps    = mutableListOf<NodeDependency>()
        private var descr   = ""

        fun description(text: String) { descr = text }

        fun attr(
            name: String, type: String,
            reactive: Boolean = false,
            mutable: Boolean  = false,
            semantics: DataFlowSemantics? = null
        ) = attrs.add(NodeAttribute(name, type, reactive, mutable, semantics = semantics))

        fun func(
            name: String,
            returnType: String,
            params: List<Pair<String, String>> = emptyList(),
            suspendable: Boolean = false,
            semantics: DataFlowSemantics? = null
        ) = methods.add(
            NodeMethod(
                name,
                returnType,
                params.toNodeParams(),           // ✅ conversion
                isSuspend = suspendable,
                semantics = semantics
            )
        )

        fun dependsOn(
            targetId: String,
            type: DependencyType = DependencyType.CONSTRUCTOR_INJECTION,
            description: String  = ""
        ) = deps.add(NodeDependency(targetId, type, description = description))

        fun toNode() = ArchitectureNode(
            id   = id,
            name = id,
            layer = layer,
            type  = type,
            description = descr,
            position = Offset.Zero,
            attributes   = attrs,
            methods      = methods,
            dependencies = deps
        )

        internal fun List<Pair<String,String>>.toNodeParams() =
            map { (n,t) -> NodeParameter(n,t) }
    }
}

/*────────────────────────── DATA-FLOW SCOPE ─────────────────────────*/

@ArchDsl
class FlowScope {
    internal val flows = mutableListOf<DataFlowConnection>()

    private fun add(from: String, to: String, sem: DataFlowSemantics) {
        val (fn, fs) = from.split("#")
        val (tn, ts) = to.split("#")
        flows += DataFlowConnection(fn, fs, tn, ts, sem)
    }

    /* infix helpers */
    infix fun String.eventTo  (t: String) = add(this, t, DataFlowSemantics.Event)
    infix fun String.stateTo  (t: String) = add(this, t, DataFlowSemantics.State)
    infix fun String.responseTo(t: String) = add(this, t, DataFlowSemantics.Response)
    infix fun String.streamTo (t: String) = add(this, t, DataFlowSemantics.Stream)
    infix fun String.commandTo(t: String) = add(this, t, DataFlowSemantics.Command)
}