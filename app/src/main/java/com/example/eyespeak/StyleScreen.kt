package com.example.eyespeak

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.HashMap

class Style(bC: Color,tC:Color,cC:Color ) {
    var backgroundColor: Color = bC
    var textColor: Color = tC
    var containerColor:Color = cC

}



fun styleDictionary(styleChoice:String) : Style
{
    var styleMap = HashMap<String,Style>()
    styleMap.put("Light",Style(Color.White,Color.Black,Color(0xb8bab9)))
    styleMap.put("Dark",Style(Color.Black,Color.White,Color(0xE1E8E3)))
    return styleMap.get(styleChoice) ?: Style(Color.White,Color.Black,Color(0xE1E8E3))
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StyleScreen(navController: NavController
) {
    val context = LocalContext.current
    var scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val chosenLanguage = TranslateLanguage.fromLanguageTag("af")
    if (chosenLanguage != null) {
        println(chosenLanguage.javaClass.name)
    }
    var styleOptions  = mutableListOf("Light","Dark")
    var expanded by remember{ mutableStateOf(false) }
    var stylePageChoice = remember{mutableStateOf(Style(Color.White,Color.Black,Color(0xE1E8E3)))}
    LaunchedEffect("style_choice")
    {
        stylePageChoice.value = withContext(Dispatchers.IO)
        {
            styleDictionary(getStringValueByKey(context.dataStore,"style_choice"))
        }
    }
    var styleChoice = remember{mutableStateOf("")}
    LaunchedEffect("style_choice")
    {
        styleChoice.value = withContext(Dispatchers.IO)
        {
            getStringValueByKey(context.dataStore,"style_choice")
        }
    }
    Surface(modifier= Modifier.background(stylePageChoice.value.backgroundColor).fillMaxSize())
    {
        Scaffold(
            scaffoldState=scaffoldState,
            floatingActionButton={
                ExtendedFloatingActionButton(
                    text={ Text("Save Changes") },
                    onClick=
                    {
                        scope.launch {
                            setStringValueByKey(context.dataStore, "style_choice", styleChoice.value)
                            scaffoldState.snackbarHostState.showSnackbar("Style Changes saved!")
                        }
                    },
                    backgroundColor= stylePageChoice.value.textColor,
                    contentColor= stylePageChoice.value.backgroundColor,
                )

            },
            backgroundColor = stylePageChoice.value.backgroundColor
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally)
            {
                Text(
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Bold,
                        color=stylePageChoice.value.textColor
                    ), text = "Styling Settings"
                )
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp))
                {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                    modifier=Modifier.border(width=1.dp,color=stylePageChoice.value.textColor))
                    {

                        TextField(
                            readOnly = true,
                            value = styleChoice.value,
                            onValueChange = { },
                            label = { Text("Style",color=stylePageChoice.value.textColor) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(stylePageChoice.value.textColor)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false })
                        {
                            styleOptions.forEach { style ->
                                DropdownMenuItem(onClick = {
                                    scope.launch {
                                        styleChoice.value = style;
                                    }
                                    expanded=false
                                })
                                {
                                    Text(
                                        text = style,
                                        style = MaterialTheme.typography.body2.copy(color = Color.Black)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


