package com.example.eyespeak

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroductionScreen(navController: NavController)
{
    val guideSections = mutableListOf("Please swipe between pages","Vision to Voice, Redfined","Voice Activations","Language Translations","Get Started!")
    val guideContent = mutableListOf("",
        "Eyespeak gives people with low-vision a complete package for viewing the words of the world around them.",
        "No need to worry on visuals - Eyespeak allows you to activate text-reading with simple Google Assistant voice commands.",
        "In an unfamiliar country? Eyespeak can translate words around you - giving you a better understanding of the environment.",
        "Ready to get started? Tap the button below!"
    )
    val pageCount = 10
    var pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val context= LocalContext.current
    var styleChoice = remember{ mutableStateOf(Style(Color.White,Color.Black,Color(0xE1E8E3))) }
    LaunchedEffect("style_choice")
    {
        styleChoice.value = withContext(Dispatchers.IO)
        {
            styleDictionary(getStringValueByKey(context.dataStore,"style_choice"))
        }
    }
    Column(Modifier.fillMaxSize().background(styleChoice.value.backgroundColor),verticalArrangement=Arrangement.Center)
    {
        HorizontalPager(pageCount = guideSections.size, state = pagerState)
        { page ->
            Text(            style = MaterialTheme.typography.headlineLarge.copy(
                color = styleChoice.value.textColor
            ),text = guideSections[page],modifier = Modifier.height(650.dp).align(Alignment.CenterHorizontally).padding(6.dp));
            Text(style=MaterialTheme.typography.bodyLarge.copy
                (
                color = styleChoice.value.textColor,
                fontWeight=FontWeight.Bold
                ),modifier=Modifier.padding(12.dp),text=guideContent[page])
        }
        Row(Modifier.height(50.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center)
        {
            repeat(guideSections.size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier.padding(2.dp).clip(CircleShape).background(color)
                        .size(20.dp)

                )
            }
        }
        Button(
            shape = RoundedCornerShape(18),
            modifier = Modifier.padding(16.dp).height(60.dp).width(320.dp).border(width=2.dp,color=styleChoice.value.textColor,shape= RoundedCornerShape(18)),
            onClick={navController.navigate(Screens.Home.route)},
            colors = ButtonDefaults.buttonColors(styleChoice.value.textColor)
        ) {
            Text(
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = styleChoice.value.backgroundColor
                ),
                text="ENTER EYESPEAK"
            )
        }
    }
}