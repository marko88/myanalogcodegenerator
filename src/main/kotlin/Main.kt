import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import domain.model.TestData

@Composable
@Preview
fun App() {
    val architecture = remember { TestData.createBookLibraryArchitecture() }
    val textMeasurer = rememberTextMeasurer()
    
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Draw dependencies (arrows) first
                architecture.getAllNodes().forEach { node ->
                    node.dependencies.forEach { dep ->
                        val targetNode = architecture.getNodeById(dep.targetId)
                        targetNode?.let {
                            // Draw arrow from source to target
                            val start = node.position
                            val end = it.position
                            
                            // Calculate arrow path
                            val path = Path().apply {
                                moveTo(start.x, start.y)
                                lineTo(end.x, end.y)
                            }
                            
                            // Draw line
                            drawPath(
                                path = path,
                                color = Color.Gray,
                                style = Stroke(width = 2f)
                            )
                            
                            // Draw arrow head
                            val angle = Math.atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble())
                            val arrowLength = 10f
                            val arrowAngle = Math.PI / 6 // 30 degrees
                            
                            val arrowPath = Path().apply {
                                moveTo(end.x, end.y)
                                lineTo(
                                    end.x - arrowLength * Math.cos(angle - arrowAngle).toFloat(),
                                    end.y - arrowLength * Math.sin(angle - arrowAngle).toFloat()
                                )
                                lineTo(
                                    end.x - arrowLength * Math.cos(angle + arrowAngle).toFloat(),
                                    end.y - arrowLength * Math.sin(angle + arrowAngle).toFloat()
                                )
                                close()
                            }
                            
                            drawPath(
                                path = arrowPath,
                                color = Color.Gray
                            )
                        }
                    }
                }
                
                // Draw nodes (boxes) on top
                architecture.getAllNodes().forEach { node ->
                    // Draw box
                    drawRect(
                        color = node.color,
                        topLeft = Offset(node.position.x - 100, node.position.y - 30),
                        size = androidx.compose.ui.geometry.Size(200f, 60f)
                    )
                    
                    // Draw text using TextMeasurer
                    drawText(
                        textMeasurer = textMeasurer,
                        text = node.name,
                        topLeft = Offset(node.position.x - 90, node.position.y - 10),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Architecture Visualization"
    ) {
        App()
    }
}
