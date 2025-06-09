package myanalogcodegenerator.ui.components.canvas.node

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.ArchitectureNode
import domain.model.NodeAttribute
import myanalogcodegenerator.ui.components.canvas.node.style.NodeItemStyles
import ui.components.canvas.NodeSelectionState

@Composable
fun NodeAttributeView(
    parent: ArchitectureNode,
    attribute: NodeAttribute,
    selectionState: NodeSelectionState = NodeSelectionState.DEFAULT,
    onClick: () -> Unit
) {
    val style = NodeItemStyles.fromSelection(selectionState)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable { onClick() }
    ) {
        // Left pin (for connecting lines)
            NodePinView(
            pinId = "${parent.id}#${attribute.name}",
            color = style.textColor,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        if (selectionState == NodeSelectionState.HIGHLIGHTED || selectionState == NodeSelectionState.SELECTED) {
            println()
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "${attribute.name}: ${attribute.type}",
            fontSize = 10.sp,
            color = style.textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.height(30.dp).align(Alignment.CenterVertically).clickable {
                onClick()
            }
        )
    }
}