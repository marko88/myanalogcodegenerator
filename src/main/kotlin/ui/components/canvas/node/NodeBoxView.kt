package myanalogcodegenerator.ui.components.canvas.node

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.ArchitectureNode
import myanalogcodegenerator.domain.repository.ArchitectureRepository
import myanalogcodegenerator.ui.components.canvas.SelectableEntity
import ui.components.canvas.NodeSelectionState
import myanalogcodegenerator.ui.components.canvas.node.style.NodeBoxStyles

@Composable
fun NodeBox(
    node: ArchitectureNode,
    architectureRepository: ArchitectureRepository,
    selectionState: NodeSelectionState = NodeSelectionState.DEFAULT,
    showDetails: Boolean = true,
    modifier: Modifier = Modifier,
    boxWidth: Dp = 200.dp,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 6.dp,
    onHeightMeasured: (Int) -> Unit = {},
    onClick: (SelectableEntity) -> Unit
) {
    val style = NodeBoxStyles.fromSelection(selectionState)

    val labelColor = NodeBoxStyles.labelColorForLayer(node.layer)
    val labelTextColor = NodeBoxStyles.labelTextColorForLayer(node.layer)

    Box(
        modifier = modifier
            .width(boxWidth)
            .wrapContentHeight()
            .clip(RoundedCornerShape(cornerRadius))
            .background(style.backgroundColor)
            .graphicsLayer { alpha = style.alpha }
            .border(borderWidth, style.borderColor, RoundedCornerShape(cornerRadius))
            .onGloballyPositioned { coordinates -> onHeightMeasured(coordinates.size.height) }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .wrapContentHeight()
                .background(labelColor, shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp)
                .clickable { onClick(SelectableEntity.Node(nodeId = node.id)) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = node.name,
                color = labelTextColor,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(30.dp)
            )
        }

        if (showDetails) {
            Column(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .align(Alignment.TopStart)
                    .padding(top = 28.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                node.attributes.forEach { attribute ->
                    NodeAttributeView(
                        attribute,
                        selectionState = architectureRepository.getNodeSelectionState(
                            SelectableEntity.Attribute(node.id, attribute)
                        ),
                        onClick = { onClick(SelectableEntity.Attribute(node.id, attribute)) }
                    ) // Placeholder
                }
                node.methods.forEach { method ->
                    NodeMethodView(
                        method,
                        selectionState = architectureRepository.getNodeSelectionState(
                            SelectableEntity.Method(node.id, method)
                        ),
                        onClick = { onClick(SelectableEntity.Method(node.id, method = method)) }
                    )
                }
            }
        }
    }
}