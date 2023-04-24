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
        val sections = mutableListOf("Profile","Settings","Text Size","Log out","Guide")
        val drawerRoutes = mutableListOf(
            DrawerRoute("Home",Screens.Home.route),
            DrawerRoute("Settings",Screens.Settings.route),
            DrawerRoute("Guide",Screens.Introduction.route),
            DrawerRoute("Log out",Screens.Landing.route),
            )
        val selectedItem = remember{mutableStateOf(drawerRoutes[0])}
        val drawerState = rememberDrawerState(initialValue= DrawerValue.Closed)
        var textResponse by remember{mutableStateOf("This is where the text will appear.")}
        var currentFontSize by remember{mutableStateOf(8)}
        LaunchedEffect("default_font_size")
        {
            currentFontSize = withContext(Dispatchers.IO)
            {
                getIntValueByKey(context.dataStore,"default_font_size")
            }
        }
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
                                color = Color.Black,
                                fontSize=currentFontSize.em,
                                lineHeight=25.sp,
                            )
                        )
                    }
                }
            }
        )

    }
