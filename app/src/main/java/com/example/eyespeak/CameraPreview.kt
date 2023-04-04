package com.example.eyespeak

import android.content.Intent
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun SimpleCameraPreview() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val takePicture = remember { mutableStateOf(false) }

    val executor = ContextCompat.getMainExecutor(context)
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
            context.startActivity(intent)
            takePicture.value = false
        }
    }
}
