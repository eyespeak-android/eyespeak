package com.example.eyespeak

import android.graphics.drawable.Drawable
import android.media.Image
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.navigationmenu.ui.theme.md_theme_light_background

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
//import com.catalin.profilescreen.ui.theme.EyeSpeakTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun StyledText(text:String,width:Int,styleChoice:Style)
{
    Text(text=text,style = MaterialTheme.typography.body2.copy(
        fontWeight = FontWeight.Bold,
        color=styleChoice.textColor
    ),
        modifier = Modifier.width(width.dp))
}

@Composable
fun ProfileScreen(navController: NavController) {

    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var scaffoldState = rememberScaffoldState()
    var name by rememberSaveable { mutableStateOf("default name") }
    var username by rememberSaveable { mutableStateOf("default username") }
    var bio by rememberSaveable { mutableStateOf("default bio") }
    var styleChoice = remember{mutableStateOf(Style(Color.White,Color.Black,Color(0xE1E8E3)))}
    LaunchedEffect("style_choice")
    {
        styleChoice.value = withContext(Dispatchers.IO)
        {
            styleDictionary(getStringValueByKey(context.dataStore,"style_choice"))
        }
    }
    LaunchedEffect("name")
    {
        name = withContext(Dispatchers.IO)
        {
            getStringValueByKey(context.dataStore,"name")
        }
    }
    LaunchedEffect("username")
    {
        username = withContext(Dispatchers.IO)
        {
            getStringValueByKey(context.dataStore,"username")
        }
    }
    LaunchedEffect("bio")
    {
        bio = withContext(Dispatchers.IO)
        {
            getStringValueByKey(context.dataStore,"bio")
        }
    }
    Surface(modifier=Modifier.fillMaxSize().background(Color.White))
    {
        androidx.compose.material.Scaffold(
            scaffoldState=scaffoldState,
            floatingActionButton={
                androidx.compose.material.ExtendedFloatingActionButton(
                    text = { androidx.compose.material.Text("Save Changes") },
                    onClick=
                    {
                        scope.launch{
                            setStringValueByKey(context.dataStore,"name",name)
                            setStringValueByKey(context.dataStore,"username",username)
                            setStringValueByKey(context.dataStore,"bio",bio)
                            scaffoldState.snackbarHostState.showSnackbar("Profile settings saved!")
                        }
                    },
                    backgroundColor = styleChoice.value.textColor,
                    contentColor = styleChoice.value.backgroundColor,
                )},
            backgroundColor = styleChoice.value.backgroundColor
        )
        {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ProfileImage()

                StyledText(text = "Change profile picture", width= 100,styleChoice=styleChoice.value)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StyledText(text = "Name", 100,styleChoice=styleChoice.value)
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        colors = TextFieldDefaults.textFieldColors(
                            //backgroundColor = Color.Transparent,
                            textColor = Color.White
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StyledText(text = "Username", width = 100,styleChoice=styleChoice.value)
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        colors = TextFieldDefaults.textFieldColors(
                            //backgroundColor = Color.Transparent,
                            textColor = Color.White
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    StyledText(
                        text = "Bio", width = 100,styleChoice=styleChoice.value
                    )
                    TextField(
                        value = bio,
                        onValueChange = { bio = it },
                        colors = TextFieldDefaults.textFieldColors(
                            //backgroundColor = Color.Transparent,
                            textColor = Color.White
                        ),
                        singleLine = false,
                        modifier = Modifier.height(150.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileImage() {
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.ic_launcher_foreground
        else
            imageUri.value
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    EyeSpeakTheme {
//        ProfileScreen()
//    }
//}