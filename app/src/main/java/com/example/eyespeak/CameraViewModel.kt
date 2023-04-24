package com.example.eyespeak

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.*


class CameraViewModel: ViewModel()
{
    private val _state = mutableStateOf(CameraScreenState())
    val state: State<CameraScreenState> = _state
    private var textToSpeech: TextToSpeech?=null

    fun textToSpeech(context: Context,text:String,language:String)
    {
        var languageLocale = Locale(language)
        textToSpeech = TextToSpeech(context)
        {
            if(it==TextToSpeech.SUCCESS)
            {
                textToSpeech?.let{txtToSpeech->
                    txtToSpeech.language= languageLocale
                    txtToSpeech.setSpeechRate(1.0f)
                    txtToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null,
                    )
                }
            }
        }
    }
}
