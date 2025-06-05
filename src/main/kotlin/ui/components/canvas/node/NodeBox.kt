package myanalogcodegenerator.ui.components.canvas.node

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.ArchitectureNode
import ui.components.canvas.NodeSelectionState
import ui.components.canvas.node.NodeBoxStyles

@Composable
fun NodeBox(
    node: ArchitectureNode,
    selectionState: NodeSelectionState = NodeSelectionState.DEFAULT,
    showDetails: Boolean = true,
    modifier: Modifier = Modifier,
    boxWidth: Dp = 160.dp,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 6.dp,
    onHeightMeasured: (Int) -> Unit = {}
) {
    val style = NodeBoxStyles.fromSelection(selectionState == NodeSelectionState.SELECTED)

    Box(
        modifier = modifier
            .width(boxWidth)
            .wrapContentHeight()
            .clip(RoundedCornerShape(cornerRadius))
            .background(style.backgroundColor)
            .border(borderWidth, style.borderColor, RoundedCornerShape(cornerRadius))
            .graphicsLayer { alpha = style.alpha }
            .onGloballyPositioned { coordinates -> onHeightMeasured(coordinates.size.height) }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 4.dp, top = 4.dp)
                .height(18.dp)
                .background(style.labelColor, shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = node.name,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (showDetails) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 28.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                node.attributes.forEach {
                    NodeAttributeView(it, selectionState = NodeSelectionState.DEFAULT) // Placeholder
                }
                if (node.attributes.isNotEmpty() && node.methods.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                node.methods.forEach {
                    NodeMethodView(it, selectionState = NodeSelectionState.DEFAULT) // Placeholder
                }
            }
        }
    }
}