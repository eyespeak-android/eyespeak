package com.example.eyespeak


import androidx.compose.material.icons.filled.Menu
import androidx.compose.foundation.clickable
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.*
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private var cameraProvider : ProcessCameraProvider? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    styleChoice:Style
)
{
    Surface(modifier=Modifier.background(styleChoice.backgroundColor))
    {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
                .background(styleChoice.backgroundColor)
        )
        {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu Icon",
                tint = styleChoice.textColor,
                modifier = Modifier.size(40.dp).clickable
                {
                    scope.launch { drawerState.open() }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,context:MainActivity) {
        val sections = mutableListOf("Profile","Settings","Text Size","Log out","Guide")
        val drawerRoutes = mutableListOf(
            DrawerRoute("Home",Screens.Home.route),
            DrawerRoute("Settings",Screens.Settings.route),
            DrawerRoute("Guide",Screens.Introduction.route),
            DrawerRoute("Log out",Screens.Landing.route),
            )
        val selectedItem = remember{mutableStateOf(drawerRoutes[0])}
        val drawerState = rememberDrawerState(initialValue= DrawerValue.Closed)
        var textResponse by remember{mutableStateOf("Tap the camera screen above to get started!")}
        var currentFontSize by remember{mutableStateOf(8)}
        LaunchedEffect("default_font_size")
        {
            currentFontSize = withContext(Dispatchers.IO)
            {
                getIntValueByKey(context.dataStore,"default_font_size")
            }
        }
        var styleChoice = remember{mutableStateOf(Style(Color.White,Color.Black,Color(0xE1E8E3)))}
        LaunchedEffect("style_choice")
        {
            styleChoice.value = withContext(Dispatchers.IO)
            {
                styleDictionary(getStringValueByKey(context.dataStore,"style_choice"))
            }
        }
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState= drawerState,
            drawerContent = {
               Surface(
                   color=styleChoice.value.backgroundColor,
                   modifier=Modifier.fillMaxSize()
               )
               {
                   Column(
                       verticalArrangement = Arrangement.spacedBy(10.dp),
                       horizontalAlignment = Alignment.CenterHorizontally,
                       modifier=Modifier.padding(10.dp)
                   )
                   {
                       drawerRoutes.forEach { route ->
                           NavigationDrawerItem(
                               label = { Text(route.title) },
                               selected = route.title == selectedItem.value.title,
                               onClick = {
                                   scope.launch { drawerState.close() }
                                   navController.navigate(route.route)
                                   selectedItem.value = route
                               },
                               colors=NavigationDrawerItemDefaults.colors(unselectedContainerColor=Color.White,unselectedTextColor=Color.Black),
                               modifier=Modifier.border(width=1.dp,color=Color.Black,shape= RoundedCornerShape(50)),
                               shape = RoundedCornerShape(50)
                           )
                       }
                   }
               }
            },
            content =
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(styleChoice.value.backgroundColor),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopAppBar("App bar",drawerState,scope,styleChoice.value)
                    SimpleCameraPreview(context=context,textResponse=textResponse,responseChange={newResponse->textResponse=newResponse})
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(styleChoice.value.backgroundColor),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = textResponse,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = styleChoice.value.textColor,
                                fontSize=currentFontSize.em,
                                lineHeight=25.sp,
                            )
                        )
                    }
                }
            }
        )

    }
