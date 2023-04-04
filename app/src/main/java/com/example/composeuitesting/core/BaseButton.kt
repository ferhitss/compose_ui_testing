package com.example.composeuitesting.core

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeuitesting.R

@Composable
fun BaseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    disabledColorsEnabled: Boolean = false,
    buttonColors: ButtonColors = BaseButtonDefaults.primaryColors(),
    textStyle: TextStyle = BaseButtonDefaults.textStyle(),
    margins: PaddingValues = BaseButtonDefaults.margins,
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp)
) {
    BaseButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        disabledColorsEnabled = disabledColorsEnabled,
        buttonColors = buttonColors,
        margins = margins,
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}

@Composable
fun BaseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    disabledColorsEnabled: Boolean = false,
    buttonColors: ButtonColors = BaseButtonDefaults.primaryColors(),
    margins: PaddingValues = BaseButtonDefaults.margins,
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp),
    content: @Composable () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(if (isFocused.value) 1.15f else 1f)

    val finalButtonColors = if(disabledColorsEnabled){
        ButtonDefaults.buttonColors(
            backgroundColor = buttonColors.backgroundColor(enabled = false).value,
            disabledBackgroundColor = buttonColors.backgroundColor(enabled = false).value,
            contentColor = buttonColors.contentColor(enabled = false).value,
            disabledContentColor = buttonColors.contentColor(enabled = false).value
        )
    }else{
        buttonColors
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = finalButtonColors,
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale.value)
            .onFocusChanged { focusState -> isFocused.value = focusState.isFocused }
            .padding(margins)
            .focusable(interactionSource = interactionSource)
    ) {
        content()
    }
}

object BaseButtonDefaults {

    private val textSize: TextUnit = 22.sp

    val margins: PaddingValues = PaddingValues(vertical = 4.dp)

    @Composable
    fun primaryColors(): ButtonColors {
        return ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.base_red_button),
            disabledBackgroundColor = colorResource(id = R.color.base_red_button_disabled),
            contentColor = colorResource(id = R.color.button_text_white),
            disabledContentColor = colorResource(id = R.color.button_text_white_disabled)
        )
    }

    @Composable
    fun secondaryColors(): ButtonColors {
        return ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.base_gray_button),
            disabledBackgroundColor = colorResource(id = R.color.base_gray_button),
            contentColor = colorResource(id = R.color.button_text_white),
            disabledContentColor = colorResource(id = R.color.button_text_white_disabled)
        )
    }

    @Composable
    fun purpleColors(): ButtonColors {
        return ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.base_purple_button),
            disabledBackgroundColor = colorResource(id = R.color.base_purple_button),
            contentColor = colorResource(id = R.color.button_text_white),
            disabledContentColor = colorResource(id = R.color.button_text_white_disabled)
        )
    }

    @Composable
    fun textStyle(): TextStyle {
        return TextStyle(
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
        )
    }
}