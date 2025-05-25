package ui.components.canvas

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.ArchitectureNode
import ui.components.canvas.CanvasGUIConstants.nodeBoxWidth

@Composable
fun NodeBox(
    node: ArchitectureNode,
    state: NodeBoxState,
    showDetails: Boolean = true,
    modifier: Modifier = Modifier,
    boxWidth: Dp = nodeBoxWidth.dp,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 6.dp,
    onHeightMeasured: (Int) -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(boxWidth)
            .wrapContentHeight()
            .clip(RoundedCornerShape(cornerRadius))
            .background(state.backgroundColor)
            .border(borderWidth, state.borderColor, RoundedCornerShape(cornerRadius))
            .graphicsLayer { alpha = state.alpha }
            .onGloballyPositioned { coordinates ->
                onHeightMeasured(coordinates.size.height)
            }
    ) {
        // Pill-shaped title box at the top left, perfectly aligned
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .wrapContentSize()
                .background(state.borderColor, shape = RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = node.name,
                color = state.labelBgColor,
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }
        if (showDetails) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 20.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                if (node.attributes.isNotEmpty()) {
                    node.attributes.forEach {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 9.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 0.5.dp)
                        )
                    }
                }
                if (node.attributes.isNotEmpty() && node.methods.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                }
                if (node.methods.isNotEmpty()) {
                    node.methods.forEach {
                        Text(
                            text = it,
                            color = Color(0xFFB8FFB8),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 1.dp)
                        )
                    }
                }
            }
        }
    }
}