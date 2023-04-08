package com.example.eyespeak


import android.speech.tts.TextToSpeech
import androidx.compose.material.icons.filled.Menu
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.compose.foundation.clickable
import androidx.compose.material3.NavigationDrawerItemColors
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Sharp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.navigationmenu.ui.theme.md_theme_dark_errorContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

private var cameraProvider : ProcessCameraProvider? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    drawerState : DrawerState,
    scope: CoroutineScope
)
{
    Row(
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    )
    {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription="Menu Icon",
            tint = Color.Black,
            modifier = Modifier.size(40.dp).clickable
            {
                scope.launch{drawerState.open()}
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,context:MainActivity) {
        val sections = mutableListOf("Profile","Settings","Languages","Log out")
        val drawerRoutes = mutableListOf(
            DrawerRoute("Profile",Screens.Profile.route),
            DrawerRoute("Settings",Screens.Landing.route),
            DrawerRoute("Languages",Screens.Landing.route),
            DrawerRoute("Log out",Screens.Login.route))
        val selectedItem = remember{mutableStateOf(drawerRoutes[0])}
        val drawerState = rememberDrawerState(initialValue=DrawerValue.Closed)
        var textResponse by remember{mutableStateOf("This is where the text will appear.")}
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState= drawerState,
            drawerContent = {
               Surface(
                   color=Color.White,
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
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopAppBar("App bar",drawerState,scope)
                    SimpleCameraPreview(context=context,textResponse=textResponse,responseChange={newResponse->textResponse=newResponse})
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = textResponse,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }
                }
            }
        )

    }
