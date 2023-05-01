package com.example.eyespeak

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LandingScreen(navController: NavController) {
    val context = LocalContext.current
    var styleChoice = remember{ mutableStateOf(Style(Color.White,Color.Black,Color(0xE1E8E3))) }
    LaunchedEffect("style_choice")
    {
        styleChoice.value = withContext(Dispatchers.IO)
        {
            styleDictionary(getStringValueByKey(context.dataStore,"style_choice"))
        }
    }
    MaterialTheme{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(styleChoice.value.backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id=com.example.eyespeak.R.drawable.eyespeak),
                "Eyespeak logo",
            modifier= Modifier.size(300.dp)

        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Button(
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(16.dp).height(60.dp).width(160.dp).border(width=2.dp,color=Color.Black,shape=RoundedCornerShape(18)),
                onClick = {
                    navController.navigate(Screens.Login.route)
                },
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Text(
                    text = "LOG IN",
                    modifier = Modifier.padding(5.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }
            Button(
                shape = RoundedCornerShape(18),
                modifier = Modifier.padding(16.dp).height(60.dp).width(160.dp).border(width=2.dp,color=Color.White,shape=RoundedCornerShape(18)),
                onClick={navController.navigate(Screens.Register.route)},
                colors = ButtonDefaults.buttonColors(Color.Black)
            ) {
                Text(
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    text="SIGN UP"
                )
            }
        }
    }
}
}