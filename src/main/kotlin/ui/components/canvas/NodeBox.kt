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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NodeBox(
    name: String,
    attributes: List<String> = emptyList(),
    methods: List<String> = emptyList(),
    showDetails: Boolean = true,
    modifier: Modifier = Modifier,
    boxColor: Color = Color(0xFF23192D),
    borderColor: Color = Color(0xFFB83B5E),
    labelBgColor: Color = Color(0xFF181926),
    labelTextColor: Color = Color(0xFFF8E1F4),
    boxWidth: Dp = 480.dp,
    boxHeight: Dp = 80.dp,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 6.dp,
) {
    Box(
        modifier = modifier
            .size(width = boxWidth, height = boxHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .background(boxColor)
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
    ) {
        // Pill-shaped title box at the top left, perfectly aligned
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .background(
                    labelBgColor,
                    shape = RoundedCornerShape(
                        topStart = cornerRadius,
                        bottomEnd = cornerRadius
                    )
                )
                .border(
                    borderWidth, borderColor, RoundedCornerShape(
                        topStart = cornerRadius,
                        bottomEnd = cornerRadius
                    )
                )
                .padding(horizontal = 10.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (showDetails) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 20.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                if (attributes.isNotEmpty()) {
                    attributes.forEach {
                        Text(
                            text = it,
                            color = Color.White, // blueish for attributes
                            fontSize = 9.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 0.5.dp)
                        )
                    }
                }
                if (attributes.isNotEmpty() && methods.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                }
                if (methods.isNotEmpty()) {
                    methods.forEach {
                        Text(
                            text = it,
                            color = Color(0xFFB8FFB8), // greenish for methods
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