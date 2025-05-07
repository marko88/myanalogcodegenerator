package ui.components.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NodeBox(
    name: String,
    modifier: Modifier = Modifier,
    boxColor: Color = Color(0xFF23192D),
    borderColor: Color = Color(0xFFB83B5E),
    labelBgColor: Color = Color(0xFF23192D),
    labelTextColor: Color = Color(0xFFF8E1F4),
    boxWidth: Dp = 180.dp,
    boxHeight: Dp = 80.dp,
    borderWidth: Dp = 2.dp,
    cornerRadius: Dp = 12.dp
) {
    Box(
        modifier = modifier
            .size(width = boxWidth, height = boxHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .background(boxColor)
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
    ) {
        // Label in the top left corner
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 4.dp, y = 4.dp)
                .background(labelBgColor, shape = RoundedCornerShape(6.dp))
        ) {
            Text(
                text = name,
                color = labelTextColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
} 