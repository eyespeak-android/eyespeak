package com.example.eyespeak

import androidx.compose.foundation.background
import androidx.compose.material3.Checkbox
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var confirmPassword by remember{mutableStateOf("")}
    val context = LocalContext.current
    var privacySeen by remember{mutableStateOf(false)}
    var styleChoice = remember{ mutableStateOf(Style(Color.White,Color.Black,Color(0xE1E8E3))) }
    LaunchedEffect("style_choice")
    {
        styleChoice.value = withContext(Dispatchers.IO)
        {
            styleDictionary(getStringValueByKey(context.dataStore,"style_choice"))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(styleChoice.value.backgroundColor),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.headlineLarge.copy(
                color = styleChoice.value.textColor
            ),
            text = "Register your account"
        )
        ProvideTextStyle(TextStyle(color= styleChoice.value.textColor)) {
            OutlinedTextField(
                email,
                onValueChange = { email = it },
                label = { Text(text="Email",style=MaterialTheme.typography.bodySmall.copy(color=styleChoice.value.textColor)) },
                modifier = Modifier.width(320.dp).border(width=2.dp,color= styleChoice.value.textColor),
            )
        }
        ProvideTextStyle(TextStyle(color= styleChoice.value.textColor)) {
            OutlinedTextField(
                password,
                onValueChange = { password = it },
                label = { Text(text="Password",style=MaterialTheme.typography.bodySmall.copy(color=styleChoice.value.textColor)) },
                modifier= Modifier.width(320.dp).border(width=2.dp,color= styleChoice.value.textColor),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
        }
        ProvideTextStyle(TextStyle(color= styleChoice.value.textColor)) {
            OutlinedTextField(
                confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text="Re-Enter",style=MaterialTheme.typography.bodySmall.copy(color=styleChoice.value.textColor)) },
                modifier= Modifier.width(320.dp).border(width=2.dp,color= styleChoice.value.textColor),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
        }
        Row(
            modifier = Modifier.padding(40.dp)
        )
        {
            Text(
                style = MaterialTheme.typography.bodyMedium.copy(
                    color=styleChoice.value.textColor,

                ),
                text="By clicking on the signing up button, You agree to Eyespeak's Terms of Service & Privacy Policy.")
            Checkbox(
                checked=privacySeen,
                onCheckedChange={privacySeen=it},
                colors=CheckboxDefaults.colors(styleChoice.value.textColor)
            )
        }
        Button(
            shape = RoundedCornerShape(18),
            modifier = Modifier.padding(16.dp).height(60.dp).width(320.dp).border(width=2.dp,color= styleChoice.value.textColor,shape= RoundedCornerShape(18)),
            onClick={navController.navigate(Screens.Introduction.route)},
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text(
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = styleChoice.value.textColor
                ),
                text="LOG IN"
            )
        }

    }
}