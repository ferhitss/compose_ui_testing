package com.example.composeuitesting.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import com.example.composeuitesting.R

@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    errorMessageProperties: ErrorMessageProperties = ErrorMessageProperties(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions? = null,
    margins: PaddingValues = PaddingValues(vertical = 10.dp),
    delayTimeMillis: Long = 0,
    lastChangeText: (String) -> Unit = {},
    icon: Int = 0,
    errorContentDescription: String = ""
){
    var isFocused: Boolean by remember { mutableStateOf(false) }
    var containerSize: Size by remember { mutableStateOf(Size.Zero) }

    var borderWidth = 0.dp
    var borderColor = Color.Transparent

    if(isError){
        borderWidth = 2.dp
        borderColor = colorResource(id = R.color.input_border_error)
    }

    if(isFocused){
        borderWidth = 2.dp
        borderColor = colorResource(id = R.color.input_border_focus)
    }

    Box(modifier = modifier
        .fillMaxWidth()
        .padding(margins)
        .onGloballyPositioned { coordinates -> containerSize = coordinates.size.toSize() },
    ) {
        NativeEditText(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.input_background),
                    shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(
                    vertical = 10.dp,
                    horizontal = if(icon == 0) 22.dp else 8.dp
                )
                .onFocusChanged { focusState -> isFocused = focusState.isFocused }
                .then(inputModifier),
            enabled = enabled,
            isPassword = isPassword,
            placeholder = placeholder,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions ?: KeyboardActions.Default,
            delayTimeMillis = delayTimeMillis,
            lastChangeText = lastChangeText,
            icon = icon
        )

        ErrorMessage(
            visible = isError,
            message = errorMessage,
            containerSize = containerSize,
            properties = errorMessageProperties,
            contentDescription = errorContentDescription
        )

    }
}
