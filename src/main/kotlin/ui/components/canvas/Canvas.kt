package ui.components.canvas

import NodeConnections
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import domain.model.ArchitectureLayer
import domain.model.ArchitectureNode
import domain.model.ArchitectureDefinitionModel
import androidx.compose.ui.input.key.*
import androidx.compose.foundation.interaction.MutableInteractionSource

@Composable
fun Canvas(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var scale by remember { mutableStateOf(1.0f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    println("Zoom: $zoom, Pan: $pan") // Debug print
                    scale = (scale * zoom).coerceIn(0.1f, 5f)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
        ) {
            content()
        }
    }
}

@Composable
fun NodeLayout(
    nodesByLayer: List<Pair<ArchitectureLayer, List<ArchitectureNode>>>,
    architecture: ArchitectureDefinitionModel,
    onNodeSelected: (String) -> Unit
) {
    val state = remember { NodeLayoutState(architecture) }
    var isCmdPressed by remember { mutableStateOf(false) }
    var hoveredNodeId by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { event ->
                when (event.type) {
                    KeyEventType.KeyDown -> {
                        if (event.key == Key.MetaLeft || event.key == Key.MetaRight) {
                            isCmdPressed = true
                        }
                    }
                    KeyEventType.KeyUp -> {
                        if (event.key == Key.MetaLeft || event.key == Key.MetaRight) {
                            isCmdPressed = false
                            state.stopConnecting()
                            hoveredNodeId = null
                        }
                    }
                }
                false
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                state.stopConnecting()
                state.selectNode(null)
                hoveredNodeId = null
            }
    ) {
        // Draw connections
        NodeConnections(
            selectedNode = state.selectedNode,
            architecture = architecture,
            nodePositions = state.nodePositions,
            nodeSizes = state.nodeSizes,
            modifier = Modifier.fillMaxSize()
        )

        // Render nodes
        var currentY = 20f
        nodesByLayer.forEach { (layer, nodes) ->
            var currentX = 20f
            
            nodes.forEach { node ->
                val isHighlighted = node.id in state.highlightIds
                val isBlurred = state.selectedNodeId != null && !isHighlighted
                val isConnecting = node.id == state.connectingNodeId
                val isHovered = node.id == hoveredNodeId
                
                val nodeState = when {
                    isConnecting -> NodeBoxState.CONNECTING
                    isHovered && isCmdPressed -> NodeBoxState.CONNECTING
                    isHighlighted -> NodeBoxState.HIGHLIGHTED
                    isBlurred -> NodeBoxState.BLURRED
                    else -> NodeBoxState.NORMAL
                }
                
                Box(
                    modifier = Modifier
                        .offset(
                            x = currentX.dp,
                            y = currentY.dp
                        )
                        .onGloballyPositioned { coordinates ->
                            val position = coordinates.positionInParent()
                            state.updateNodePosition(
                                node.id,
                                position
                            )
                            state.updateNodeSize(
                                node.id,
                                Offset(
                                    coordinates.size.width.toFloat(),
                                    coordinates.size.height.toFloat()
                                )
                            )
                        }
                ) {
                    NodeBox(
                        node = node,
                        state = nodeState,
                        showDetails = true,
                        modifier = Modifier
                            .background(Color.Transparent)
                            .clickable { 
                                if (isCmdPressed) {
                                    state.startConnecting(node.id)
                                } else {
                                    state.selectNode(node.id)
                                    onNodeSelected(node.id)
                                }
                            }
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        when (event.type) {
                                            androidx.compose.ui.input.pointer.PointerEventType.Enter -> {
                                                if (isCmdPressed) {
                                                    hoveredNodeId = node.id
                                                }
                                            }
                                            androidx.compose.ui.input.pointer.PointerEventType.Exit -> {
                                                hoveredNodeId = null
                                            }
                                            else -> {}
                                        }
                                    }
                                }
                            }
                    )
                }
                
                currentX += CanvasGUIConstants.nodeBoxWidth + 20f
            }

            val maxHeight = nodes.maxOfOrNull { state.nodeSizes[it.id]?.y ?: 0f } ?: 0f
            currentY += maxHeight/2 + 40f
        }
    }
} 