package com.example.eyespeak

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
//import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.navigationmenu.ui.theme.EyespeakTheme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*


class MainActivity : ComponentActivity() {


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    )
    { isGranted -> {
            if(isGranted) {
                println("Permission granted")
            }
        else {
            println("Permission denied")
            }
        }
    }
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("kilo", "Permission previously granted")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> Log.i("kilo", "Show camera permissions dialog")

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    fun startCamera()
    {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if(result.resultCode== Activity.RESULT_OK)
        {
            val data: Intent? = result.data
            if(data!=null&&data.getExtras()!=null)
            {
                println("Image data: $data")
            }
        }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EyespeakTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController= rememberNavController()
                    NavGraph(navController=navController,context=this)
                }
            }
        }
        requestCameraPermission()

    }

}

//private val language = listOf("English", "Italian")

//@Composable
//fun MultiLanguage(
//    viewModel: LanguageViewModel = viewModel(
//        factory = DataStoreViewModelFactory(DataStorePreferenceRepository(LocalContext.current))
//    )
//) {
//    val scope = rememberCoroutineScope()
//    val currentLanguage = viewModel.language.observeAsState().value
//    val menuExpanded = remember { mutableStateOf(false) }
//
//    SetLanguage(position = currentLanguage!!)
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "MultiLanguage",
//                        fontSize = 20.sp,
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center
//                    )
//                },
//                actions = {
//                    IconButton(onClick = { menuExpanded.value = true }) {
//                        Icon(
//                            Icons.Filled.MoreVert,
//                            contentDescription = "Menu",
//                            tint = Color.White
//                        )
//                    }
//                    Column(
//                        modifier = Modifier.wrapContentSize(TopStart)
//                    ) {
//                        DropdownMenu(
//                            expanded = menuExpanded.value,
//                            onDismissRequest = {
//                                menuExpanded.value = false
//                            },
//                            modifier = Modifier
//                                .width(150.dp)
//                                .wrapContentSize(TopStart)
//                        ) {
//                            DropdownMenuItem(onClick = { /*TODO*/ }) {
//                                Text(text = stringResource(id = R.string.versions))
//                            }
//                            DropdownMenuItem(onClick = { /*TODO*/ }) {
//                                Text(text = stringResource(id = R.string.settings))
//                            }
//                            DropdownMenuItem(onClick = { /*TODO*/ }) {
//                                Text(text = stringResource(id = R.string.logout))
//                            }
//                        }
//                    }
//                }
//            )
//        }
//    ) {
//        Column(
//            modifier = Modifier
//                .verticalScroll(rememberScrollState(0))
//                .fillMaxSize()
//        ) {
//            Spacer(modifier = Modifier.height(8.dp))
//            LanguagePicker(currentLanguage) { selected ->
//                scope.launch {
//                    viewModel.saveLanguage(selected)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun LanguagePicker(
//    selectedPosition: Int,
//    onLanguageSelected: (Int) -> Unit
//) {
//    Card(
//        modifier = Modifier.fillMaxSize(),
//        shape = RoundedCornerShape(16.dp),
//        elevation = 0.dp
//    ) {
//        LanguageContentPortrait(selectedPosition, onLanguageSelected)
//    }
//}

//@Composable
//fun LanguageContentPortrait(
//    selectedPosition: Int,
//    onLanguageSelected: (Int) -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(100.dp))
//        ToggleGroup(selectedPosition = selectedPosition, onClick = onLanguageSelected)
//        Spacer(modifier = Modifier.height(60.dp))
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = stringResource(id = R.string.content),
//                modifier = Modifier.fillMaxSize(),
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
//
//@Composable
//private fun SetLanguage(position: Int) {
//    val locale = Locale(if (position == 1) "it" else "en")
//    val configuration = LocalConfiguration.current
//    configuration.setLocale(locale)
//    val resources = LocalContext.current.resources
//    resources.updateConfiguration(configuration, resources.displayMetrics)
//}
//
//@Composable
//private fun ToggleGroup(
//    selectedPosition: Int,
//    onClick: (Int) -> Unit
//) {
//    val shape = RoundedCornerShape(4.dp)
//    Row(
//        modifier = Modifier
//            .padding(vertical = 8.dp)
//            .clip(shape)
//            .border(1.dp, Color(0xFFAAAAAA), shape)
//    ) {
//        language.forEachIndexed { index, element ->
//            val verticalPadding = if (index == selectedPosition) 8.dp else 0.dp
//            TextUnit(
//                text = element,
//                color = if (index != selectedPosition) Color.Black else Color.White,
//                modifier = Modifier
//                    .align(CenterVertically)
//                    .run {
//                        if (index != selectedPosition) this
//                        else background(MaterialTheme.colors.primary).border(1.dp, Color.Gray)
//                    }
//                    .clickable(
//                        onClick = { onClick(index) },
//                        role = Role.RadioButton
//                    )
//                    .padding(horizontal = 16.dp, vertical = verticalPadding)
//            )
//        }
//    }
//}
//
//class DataStoreViewModelFactory(private val dataStorePreferenceRepository: DataStorePreferenceRepository):
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
//            return LanguageViewModel(dataStorePreferenceRepository) as T
//        }
//        throw IllegalStateException()
//    }
//}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    EyespeakTheme {
//        Greeting("Android")
//    }
//}