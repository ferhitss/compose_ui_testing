package com.example.composeuitesting.core

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.composeuitesting.R

class ErrorMessageProperties constructor(
    val minWidth: Dp = 300.dp
)

@Composable
fun ErrorMessage(
    visible: Boolean,
    message: String,
    containerSize: Size,
    properties: ErrorMessageProperties = ErrorMessageProperties(),
    contentDescription: String = ""
){
    ErrorMessage(
        visible = visible,
        containerSize = containerSize,
        properties = properties,
        contentDescription = contentDescription
    ) {
        Text(
            text = message,
            color = Color.Red,
            modifier = Modifier
                .testTag("error_message_text_tag")
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(10.dp)
        )
    }
}

@Composable
fun ErrorMessage(
    visible: Boolean,
    containerSize: Size,
    properties: ErrorMessageProperties = ErrorMessageProperties(),
    contentDescription: String = "",
    content: @Composable () -> Unit
){
    if(!visible) return

    val isKeyboardOpen by KeyboardVisibility.isKeyboardOpen()
    if(isKeyboardOpen) return

    val containerWidth: Dp = with(LocalDensity.current){ containerSize.width.toDp() }
    val containerHeight: Int = containerSize.height.toInt()

    val arrowWidth: Dp = 15.dp
    val arrowHeight: Dp = (arrowWidth.value * 0.7).dp
    val arrowXOffset: Dp = ((containerWidth - arrowWidth) / 2f)
    val contentYOffset: Dp = arrowHeight

    val contentWidth = if(containerWidth < properties.minWidth) properties.minWidth else containerWidth

    Popup(
        alignment = Alignment.TopStart,
        offset = IntOffset(0, containerHeight)
    ) {
        Box(modifier = Modifier
            .width(contentWidth)
            .semantics(mergeDescendants = true) { this.contentDescription = contentDescription }
        ){
            Arrow(
                width = arrowWidth,
                height = arrowHeight,
                color = colorResource(id = R.color.input_background_error),
                modifier = Modifier.offset(arrowXOffset)
            )
            Box(modifier = Modifier.offset(y = contentYOffset)){
                content()
            }
        }
    }
}

@Composable
private fun Arrow(
    width: Dp,
    height: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier
        .width(width)
        .height(height - 1.dp)
    ){
        drawPath(
            color = color,
            path = Path().apply {
                moveTo(width.toPx() / 2f, 0f)
                lineTo(width.toPx(), height.toPx())
                lineTo(0f, height.toPx())
            }
        )
    }
}