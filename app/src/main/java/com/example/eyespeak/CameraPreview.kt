package com.example.eyespeak

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*



@Composable
fun SimpleCameraPreview(context:MainActivity,textResponse: String,responseChange:(String) ->Unit, viewModel:CameraViewModel = viewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val allLanguages= getLanguageDictionary()
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val takePicture = remember { mutableStateOf(false) }
    val state = viewModel.state.value
    val executor = ContextCompat.getMainExecutor(context)
    val fromLanguageChoice = remember{mutableStateOf("English")}
    val toLanguageChoice = remember{mutableStateOf("English")}
    LaunchedEffect("from_language")
    {
        fromLanguageChoice.value = withContext(Dispatchers.IO)
        {
            getStringValueByKey(context.dataStore,"from_language").toString()
        }
    }
    LaunchedEffect("to_language")
    {
        toLanguageChoice.value = withContext(Dispatchers.IO)
        {
            getStringValueByKey(context.dataStore,"to_language").toString()
        }
    }
    println("from_language: $fromLanguageChoice")
    println("to_language: $toLanguageChoice")
    val cameraProvider = remember { mutableStateOf<ProcessCameraProvider?>(null) }
    val imageCapture = remember { mutableStateOf<ImageCapture?>(null) }
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            cameraProviderFuture.addListener({
                cameraProvider.value = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                imageCapture.value = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                cameraProvider.value?.unbindAll()
                cameraProvider.value?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture.value
                )
            }, executor)

            previewView
        },
        modifier = Modifier
            .height(500.dp)
            .clickable { takePicture.value = true }
            .fillMaxSize(),
    )

    LaunchedEffect(takePicture.value) {
        if (takePicture.value) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val result = (context as MainActivity).activityResultRegistry.register("takePicture",
                ActivityResultContracts.StartActivityForResult())
            { result ->
                if(result.resultCode == Activity.RESULT_OK)
                {
                    var translationMessage = ""
                    val image = result.data?.extras?.get("data") as Bitmap
                    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    val realImage = InputImage.fromBitmap(image,0)
                    val textResult = recognizer.process(realImage)
                        .addOnSuccessListener{visionText ->
                            println("Detected text:"+visionText.text)
                            val options = TranslatorOptions.Builder()
                                .setSourceLanguage(fromLanguageChoice.value)
                                .setTargetLanguage(toLanguageChoice.value)
                                .build()
                            val translator = Translation.getClient(options)
                            var conditions = DownloadConditions.Builder()
                                .requireWifi()
                                .build()
                            translator.downloadModelIfNeeded(conditions)
                                .addOnSuccessListener{
                                    Toast.makeText(context,"Model downloaded!",Toast.LENGTH_SHORT)
                                }
                                .addOnFailureListener{exception->
                                    Toast.makeText(context,"Model download failed.",Toast.LENGTH_SHORT)
                                }
                            translator.translate(visionText.text)
                                .addOnSuccessListener{translatedText->
                                    translationMessage = translatedText
                                    println("Translated text: $translatedText")
                                    responseChange(translatedText)
                                    viewModel.textToSpeech(context,translatedText,toLanguageChoice.value)
                                }
                        }.addOnFailureListener{e->
                            println("Detected text has some kind of error.")
                        }
                    println("Bitmap: $image")
                }
            }
            intent.resolveActivity(context.packageManager)?.also{
                result.launch(intent)
            }
            //context.startActivity(intent)
            takePicture.value = false
        }
    }
}
