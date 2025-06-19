package com.example.bluvault.components

import FamilijenGrotesk
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextFieldComponent(
    placeholder: String,
    text: String,
    onValueChange: (String) -> Unit,
    keyboardOpt: KeyboardOptions = KeyboardOptions.Default,
    visualTrans: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.TextField(
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(
                text = placeholder,
                fontFamily = FamilijenGrotesk,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(bottom = 6.dp)
            )
        },
        keyboardOptions = keyboardOpt,
        visualTransformation = visualTrans,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 18.sp),
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = FamilijenGrotesk,
                color = Color.Gray,
                fontSize = 16.sp,
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor         = Color.White,
            unfocusedTextColor       = Color.White,

            focusedContainerColor    = Color.Transparent,
            unfocusedContainerColor  = Color.Transparent,

            cursorColor              = Color.White,

            focusedIndicatorColor    = Color.White,
            unfocusedIndicatorColor  = Color.Gray,

            focusedLabelColor        = Color.White,
            unfocusedLabelColor      = Color.Gray
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical= 8.dp)
    )
}
