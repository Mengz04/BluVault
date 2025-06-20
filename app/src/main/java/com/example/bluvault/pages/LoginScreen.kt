package com.example.bluvault.pages

import FamilijenGrotesk
import LoginWithEmail
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bluvault.R
import com.example.bluvault.components.TextFieldComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030B1C))
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedButton(
                onClick = { navController.navigate("Welcome") },
                modifier = Modifier
                    .height(44.dp)
                    .width(44.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "left arrow",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = R.drawable.bluvalut_icon),
                contentDescription = "bluVault logo",
                modifier = Modifier
                    .width(120.dp)
                    .height(40.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Log In",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp,
                lineHeight = 36.sp,
                fontFamily = FamilijenGrotesk
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                    text = "Please input your account information",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = FamilijenGrotesk,
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextFieldComponent(
                text = email,
                onValueChange = {email = it},
                label = "Email",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )

            TextFieldComponent(
                text = password,
                onValueChange = {password = it},
                label = "Password",
                keyboardOpt = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTrans = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                onClick = clickable@{
                    LoginWithEmail(
                        email = email,
                        password = password,
                        onSuccess = {
                            Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
                            navController.navigate("Home")
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            ) {
                Text(
                    text = "Log In",
                    color = Color(0xFF030B1C),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FamilijenGrotesk,
                    fontSize = 16.sp,
                )
            }
        }
    }

}
