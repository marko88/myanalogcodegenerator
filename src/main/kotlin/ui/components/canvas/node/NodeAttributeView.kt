package myanalogcodegenerator.ui.components.canvas.node


import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.NodeAttribute
import myanalogcodegenerator.ui.components.canvas.node.style.NodeItemStyles
import ui.components.canvas.NodeSelectionState

@Composable
fun NodeAttributeView(
    attribute: NodeAttribute,
    selectionState: NodeSelectionState = NodeSelectionState.DEFAULT
) {
    val style = NodeItemStyles.methodStyle(selectionState == NodeSelectionState.SELECTED)

    Text(
        text = attribute.name,
        color = style.textColor,
        fontSize = 9.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(bottom = 1.dp)
    )
}