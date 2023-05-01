package com.example.eyespeak

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="settings")




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextScreen(navController: NavController) {

    val context = LocalContext.current
    var defaultFontSize = 8
    val grabFontSize = intPreferencesKey("default_font_size")
    var sliderPosition by remember{mutableStateOf(0f)}
    var currentFontSize by remember{mutableStateOf(8)}
    LaunchedEffect("default_font_size")
    {
        currentFontSize = withContext(Dispatchers.IO)
        {
            getIntValueByKey(context.dataStore,"default_font_size")
        }
    }
    LaunchedEffect("font_slider_position")
    {
        sliderPosition = withContext(Dispatchers.IO)
        {
            getFloatValueByKey(context.dataStore,"font_slider_position")
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
    var scaffoldState = rememberScaffoldState()
    var fontDisplay by remember{mutableStateOf(defaultFontSize+(defaultFontSize*sliderPosition).toInt())}
    var progressCount: Int by remember { mutableStateOf(0) }
    var progress by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    /* to avoid the direct calculation of progress variable which is a Float
     and it can sometimes cause problems like it shows 0.4 to 0.400004 so, here I have use
     progressCount and we will increase and decrease it and then convert it to progress(Float)
     and then use that progress with our ProgressBar Width*/
    when (progressCount) {
        0 -> progress = 0.0f
        1 -> progress = 0.1f
        2 -> progress = 0.2f
        3 -> progress = 0.3f
        4 -> progress = 0.4f
        5 -> progress = 0.5f
        6 -> progress = 0.6f
        7 -> progress = 0.7f
        8 -> progress = 0.8f
        9 -> progress = 0.9f
        10 -> progress = 1.0f
    }

    val size by animateFloatAsState(
        targetValue = progress,
        tween(
            durationMillis = 1000,
            delayMillis = 200,
            easing = LinearOutSlowInEasing
        )
    )

    Surface(modifier=Modifier.background(styleChoice.value.backgroundColor))
    {
        androidx.compose.material.Scaffold(
            scaffoldState=scaffoldState,
            floatingActionButton={
                androidx.compose.material.ExtendedFloatingActionButton(
                    text = { androidx.compose.material.Text("Save Changes") },
                    onClick=
                    {

                        scope.launch{
                            fontDisplay = defaultFontSize+(defaultFontSize*sliderPosition).toInt()
                            setIntValueByKey(context.dataStore, "default_font_size", fontDisplay)
                            scaffoldState.snackbarHostState.showSnackbar("Font size saved!")
                        }
                    },
                    contentColor = styleChoice.value.backgroundColor,
                    backgroundColor = styleChoice.value.textColor,
                )},
            backgroundColor = styleChoice.value.backgroundColor
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text="Font Sizing",
                    style=MaterialTheme.typography.titleLarge.copy(
                        color = styleChoice.value.textColor
                    )
                )
                Row(modifier = Modifier.widthIn(min = 300.dp).fillMaxWidth(size).padding(10.dp))
                {
                    Text(
                        text = "Use the scroller below. Changes will reflect in this text. \n Current size: ${currentFontSize}",
                        style = TextStyle(
                            fontSize = (defaultFontSize + (defaultFontSize * sliderPosition)).toInt().em,
                            color = styleChoice.value.textColor
                        )
                    )
                }
                Divider(startIndent=8.dp,color=styleChoice.value.textColor)
                // for the text above the progressBar
                Row(
                    modifier = Modifier
                        .widthIn(min = 300.dp)
                        .fillMaxWidth(size)
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Slider(value = sliderPosition,
                        onValueChange =
                        {
                            sliderPosition = it;
                            scope.launch{
                                fontDisplay = defaultFontSize+(defaultFontSize*sliderPosition).toInt()
                                setFloatValueByKey(context.dataStore, "font_slider_position", sliderPosition)
                            }
                        }
                    )
                }


            }
        }
    }

//    Use this when you want your progress bar should animate when you open your app
//    LaunchedEffect(key1 = true) {
//        progressCount = 7
//    }

}

