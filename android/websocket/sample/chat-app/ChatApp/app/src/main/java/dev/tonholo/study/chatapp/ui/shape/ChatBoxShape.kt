package dev.tonholo.study.chatapp.ui.shape

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

val CHAT_BOX_SHAPE_ARROW_SIZE = 4.dp
val CHAT_BOX_SHAPE_ARROW_HEIGHT = 8.dp

class ChatBoxShape(
    private val cornerRadius: Dp,
    private val onRightSide: Boolean = true,
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline =
        Outline.Generic(
            path = if (onRightSide) {
                arrowOnRightSidePath(size = size, density = density)
            } else {
                arrowOnLeftSidePath(size = size, density = density)
            }
        )

    private fun arrowOnRightSidePath(size: Size, density: Density): Path {
        fun Dp.toFloat() = with(density) {
            toPx()
        }

        return Path().apply {
            reset()

            val cornerRadiusPx = cornerRadius.toFloat()
            val arrowSize = CHAT_BOX_SHAPE_ARROW_SIZE.toFloat()
            val arrowHeight = CHAT_BOX_SHAPE_ARROW_HEIGHT.toFloat()
            val right = size.width - arrowSize

            arcTo(
                Rect(
                    left = 0f,
                    top = 0f,
                    right = cornerRadiusPx,
                    bottom = cornerRadiusPx,
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )
            lineTo(x = size.width, y = 0f)
            lineTo(x = right, arrowHeight)
            lineTo(x = right, size.height - cornerRadiusPx)
            arcTo(
                Rect(
                    left = right - cornerRadiusPx,
                    top = size.height - cornerRadiusPx,
                    right = right,
                    bottom = size.height,
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )
            lineTo(x = cornerRadiusPx, size.height)
            arcTo(
                Rect(
                    left = 0f,
                    top = size.height - cornerRadiusPx,
                    right = cornerRadiusPx,
                    bottom = size.height,
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )

            close()
        }
    }

    private fun arrowOnLeftSidePath(size: Size, density: Density): Path {
        fun Dp.toFloat() = with(density) {
            toPx()
        }

        return Path().apply {
            reset()

            val cornerRadiusPx = cornerRadius.toFloat()
            val arrowSize = CHAT_BOX_SHAPE_ARROW_SIZE.toFloat()
            val arrowHeight = CHAT_BOX_SHAPE_ARROW_HEIGHT.toFloat()

            lineTo(x = size.width, y = 0f)

            arcTo(
                Rect(
                    left = size.width - cornerRadiusPx,
                    top = 0f,
                    right = size.width,
                    bottom = cornerRadiusPx,
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            ) // right top corner
            lineTo(x = size.width, y = size.height - cornerRadiusPx)
            arcTo(
                Rect(
                    left = size.width - cornerRadiusPx,
                    top = size.height - cornerRadiusPx,
                    right = size.width,
                    bottom = size.height,
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            ) // right bottom corner
            lineTo(x = arrowSize + cornerRadiusPx, y = size.height)
            arcTo(
                Rect(
                    left = arrowSize,
                    top = size.height - cornerRadiusPx,
                    right = arrowSize + cornerRadiusPx,
                    bottom = size.height,
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            ) // left bottom corner

            lineTo(x = arrowSize, y = arrowHeight)
            lineTo(x = 0f, y = 0f)
            close()
        }
    }
}

@Preview
@Composable
private fun RightSideChatBoxShapePreview() {
    ChatAppTheme {
        Surface(
            shape = ChatBoxShape(4.dp),
            modifier = Modifier
                .width(100.dp)
                .padding(16.dp)
        ) {
            Text(
                text = "Test",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = CHAT_BOX_SHAPE_ARROW_SIZE)
            )
        }
    }
}

@Preview
@Composable
private fun LeftSideChatBoxShapePreview() {
    ChatAppTheme {
        Surface(
            shape = ChatBoxShape(
                cornerRadius = 4.dp,
                onRightSide = false,
            ),
            modifier = Modifier
                .width(100.dp)
                .padding(16.dp)
        ) {
            Text(
                text = "Test",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = CHAT_BOX_SHAPE_ARROW_SIZE)
            )
        }
    }
}
