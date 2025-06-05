package myanalogcodegenerator.ui.components.canvas.node


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.NodeAttribute
import ui.components.canvas.NodeSelectionState
import ui.components.canvas.node.NodeBoxStyles

@Composable
fun NodeAttributeView(
    attribute: NodeAttribute,
    selectionState: NodeSelectionState = NodeSelectionState.DEFAULT
) {
    val style = NodeBoxStyles.fromSelection(selectionState == NodeSelectionState.SELECTED)

    Text(
        text = attribute.name,
        color = style.labelColor,
        fontSize = 9.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(bottom = 1.dp)
    )
}