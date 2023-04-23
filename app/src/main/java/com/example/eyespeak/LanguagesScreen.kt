package com.example.eyespeak

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.util.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val language = listOf("English", "Italian")



    fun getLanguageDictionary() : Map<String,String>
    {
        val languageDictionary = mapOf(
            "af" to "Afrikaans",
            "am" to "Amharic",
            "ar" to "Arabic",
            "az" to "Azerbaijani",
            "be" to "Belarusian",
            "bg" to "Bulgarian",
            "bn" to "Bengali",
            "bs" to "Bosnian",
            "ca" to "Catalan",
            "ceb" to "Cebuano",
            "co" to "Corsican",
            "cs" to "Czech",
            "cy" to "Welsh",
            "da" to "Danish",
            "de" to "German",
            "el" to "Greek",
            "en" to "English",
            "eo" to "Esperanto",
            "es" to "Spanish",
            "et" to "Estonian",
            "eu" to "Basque",
            "fa" to "Persian",
            "fi" to "Finnish",
            "fr" to "French",
            "fy" to "Frisian",
            "ga" to "Irish",
            "gd" to "Scots Gaelic",
            "gl" to "Galician",
            "gu" to "Gujarati",
            "ha" to "Hausa",
            "haw" to "Hawaiian",
            "he" to "Hebrew",
            "hi" to "Hindi",
            "hmn" to "Hmong",
            "hr" to "Croatian",
            "ht" to "Haitian Creole",
            "hu" to "Hungarian",
            "hy" to "Armenian",
            "id" to "Indonesian",
            "ig" to "Igbo",
            "is" to "Icelandic",
            "it" to "Italian",
            "iw" to "Hebrew",
            "ja" to "Japanese",
            "jv" to "Javanese",
            "ka" to "Georgian",
            "kk" to "Kazakh",
            "km" to "Khmer",
            "kn" to "Kannada",
            "ko" to "Korean",
            "ku" to "Kurdish",
            "ky" to "Kyrgyz",
            "la" to "Latin",
            "lb" to "Luxembourgish",
            "lo" to "Lao",
            "lt" to "Lithuanian",
            "lv" to "Latvian",
            "mg" to "Malagasy",
            "mi" to "Maori",
            "mk" to "Macedonian",
            "ml" to "Malayalam",
            "mn" to "Mongolian",
            "mr" to "Marathi",
            "ms" to "Malay",
            "mt" to "Maltese",
            "my" to "Burmese",
            "ne" to "Nepali",
            "nl" to "Dutch",
            "no" to "Norwegian",
            "ny" to "Chichewa",
            "or" to "Odia",
            "pa" to "Punjabi",
            "pl" to "Polish",
            "ps" to "Pashto",
            "pt" to "Portuguese",
            "ro" to "Romanian",
            "ru" to "Russian",
            "rw" to "Kinyarwanda",
            "sd" to "Sindhi",
            "si" to "Sinhala",
            "sk" to "Slovak",
            "sl" to "Slovenian",
            "sm" to "Samoan",
            "sn" to "Shona",
            "so" to "Somali",
            "sq" to "Albanian",
            "sr" to "Serbian",
            "st" to "Sesotho",
            "su" to "Sundanese",
            "sv" to "Swedish",
            "sw" to "Swahili",
            "ta" to "Tamil",
            "te" to "Telugu",
            "tg" to "Tajik",
            "th" to "Thai",
            "tk" to "Turkmen",
            "tl" to "Tagalog",
            "tr" to "Turkish",
            "tt" to "Tatar",
            "ug" to "Uyghur",
            "uk" to "Ukrainian",
            "ur" to "Urdu",
            "uz" to "Uzbek",
            "vi" to "Vietnamese",
            "xh" to "Xhosa",
            "yi" to "Yiddish",
            "yo" to "Yoruba",
            "zh" to "Chinese",
            "zu" to "Zulu"
        )
        return languageDictionary
    }
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun LanguageScreen(navController: NavController
    ) {
        val context = LocalContext.current
        var scaffoldState = rememberScaffoldState()
        val allLanguages = getLanguageDictionary()
        println(allLanguages)
        val supportedLanguages = TranslateLanguage.getAllLanguages()
        val languageOptions = mutableListOf<String>()
        val languageConversions = HashMap<String,String>()
        supportedLanguages.map{language->
            allLanguages.get(language)?.let { languageOptions.add(it) }
            allLanguages.get(language)?.let { languageConversions.put(it,language) }
        }
        println(languageOptions)
        println(languageConversions)
        val chosenLanguage = TranslateLanguage.fromLanguageTag("af")
        if (chosenLanguage != null) {
            println(chosenLanguage.javaClass.name)
        }
        println("Languages: $supportedLanguages")
        var toExpanded by remember{mutableStateOf(false)}
        var fromExpanded by remember{mutableStateOf(false)}
        val scope = rememberCoroutineScope()
        val fromLanguageChoice = remember{mutableStateOf("English")}
        val toLanguageChoice = remember{mutableStateOf("English")}
        LaunchedEffect("from_language")
        {
            fromLanguageChoice.value = withContext(Dispatchers.IO)
            {
                allLanguages.get(getStringValueByKey(context.dataStore,"from_language")).toString()
            }
        }
        LaunchedEffect("to_language")
        {
            toLanguageChoice.value = withContext(Dispatchers.IO)
            {
                allLanguages.get(getStringValueByKey(context.dataStore,"to_language")).toString()
            }
        }
        Surface(modifier= Modifier.background(Color.White).fillMaxSize())
        {
            Scaffold(
                scaffoldState=scaffoldState,
                floatingActionButton={
                    ExtendedFloatingActionButton(
                        text={Text("Save Changes")},
                        onClick=
                        {
                            scope.launch{
                                languageConversions.get(fromLanguageChoice.value)?.let {
                                    setStringValueByKey(context.dataStore,"from_language",
                                        it
                                    )
                                }
                                languageConversions.get(toLanguageChoice.value)?.let {
                                    setStringValueByKey(context.dataStore,"to_language",
                                        it
                                    )
                                }
                                scaffoldState.snackbarHostState.showSnackbar("Language changes saved!")
                            }
                        },
                        backgroundColor=Color.Black,
                        contentColor=Color.White,
                    )

                }
            )
            {
                Column(horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text(
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Bold
                        ), text = "Language Settings"
                    )
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp))
                    {
                        ExposedDropdownMenuBox(
                            expanded = fromExpanded,
                            onExpandedChange = { fromExpanded = !fromExpanded })
                        {

                            TextField(
                                readOnly = true,
                                value = fromLanguageChoice.value,
                                onValueChange = { },
                                label = { Text("From") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = fromExpanded
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = fromExpanded,
                                onDismissRequest = { fromExpanded = false })
                            {
                                languageOptions.forEach { language ->
                                    DropdownMenuItem(onClick = {
                                        fromExpanded = false;scope.launch {
                                        fromLanguageChoice.value = language
                                    }
                                    })
                                    {
                                        Text(
                                            text = language,
                                            style = MaterialTheme.typography.body2.copy(color = Color.Black)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp))
                    {
                        ExposedDropdownMenuBox(
                            expanded = toExpanded,
                            onExpandedChange = { toExpanded = !toExpanded })
                        {

                            TextField(
                                readOnly = true,
                                value = toLanguageChoice.value,
                                onValueChange = { },
                                label = { Text("To") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = toExpanded
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = toExpanded,
                                onDismissRequest = { toExpanded = false })
                            {
                                languageOptions.forEach { language ->
                                    DropdownMenuItem(onClick = {
                                        toExpanded = false;scope.launch {
                                        toLanguageChoice.value = language
                                    }
                                    })
                                    {
                                        Text(
                                            text = language,
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