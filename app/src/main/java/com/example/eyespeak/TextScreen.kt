package com.example.eyespeak

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController


@Composable
fun TextScreen(navController: NavController) {

    var defaultFontSize = 8
    val context = LocalContext.current
    var sliderPosition by remember{mutableStateOf(0f)}
    var fontDisplay = defaultFontSize+(defaultFontSize*sliderPosition).toInt()
    var progressCount: Int by remember { mutableStateOf(0) }
    var progress by remember { mutableStateOf(0f) }

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

    Surface(modifier=Modifier.background(Color.White))
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text="Font Sizing",
                style=MaterialTheme.typography.titleLarge.copy(
                    color = Color.Black
                )
            )
            Row(modifier = Modifier.widthIn(min = 300.dp).fillMaxWidth(size).padding(10.dp))
            {
                Text(
                    text = "Use the scroller below. Changes will reflect in this text.",
                    style = TextStyle(
                        fontSize = (defaultFontSize + (defaultFontSize * sliderPosition)).toInt().em,
                        color = Color.Black
                    )
                )
            }
            Divider(startIndent=8.dp)
            // for the text above the progressBar
            Row(
                modifier = Modifier
                    .widthIn(min = 300.dp)
                    .fillMaxWidth(size)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Slider(value = sliderPosition, onValueChange = { sliderPosition = it })
            }


        }
    }

//    Use this when you want your progress bar should animate when you open your app
//    LaunchedEffect(key1 = true) {
//        progressCount = 7
//    }

}
