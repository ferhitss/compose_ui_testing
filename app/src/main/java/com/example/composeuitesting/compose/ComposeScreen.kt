package com.example.composeuitesting.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeuitesting.core.BaseButton
import com.example.composeuitesting.core.BaseButtonDefaults
import com.example.composeuitesting.core.TextInput

@Composable
fun ComposeScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Header(onClickBack = navBack)

        Column(modifier = Modifier.width(500.dp)) {
            Text(
                text = "Title",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
            Text(
                text = "Description",
                fontSize = 24.sp
            )

            TextInput(
                value = "text input value",
                onValueChange = {  },
                placeholder = "placeholder",
                isError = true,
                errorMessage = "Error message",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                margins = PaddingValues(vertical = 10.dp)
            )

            BaseButton(
                text = "Next",
                onClick = {  },
                enabled = true,
                margins = PaddingValues(top = 35.dp, bottom = 8.dp)
            )
            BaseButton(
                text = "Cancel",
                onClick = {  },
                buttonColors = BaseButtonDefaults.secondaryColors(),
                margins = PaddingValues(vertical = 8.dp)
            )

            BaseButton(
                text = "Register",
                onClick = {  },
                modifier = Modifier
                    .align(Alignment.End)
                    .width(220.dp),
                buttonColors = BaseButtonDefaults.secondaryColors(),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                margins = PaddingValues(top = 14.dp)
            )
        }
    }
}