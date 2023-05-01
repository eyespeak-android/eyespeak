package com.example.eyespeak



import android.content.Intent
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import com.example.eyespeak.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.Shapes
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navigationmenu.ui.theme.EyespeakTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun SettingsScreen(navController: NavController){
        val context = LocalContext.current;
        var styleChoice = remember{mutableStateOf(Style(Color.White,Color.Black,Color(0xE1E8E3)))}
        LaunchedEffect("style_choice")
        {
            styleChoice.value = withContext(Dispatchers.IO)
            {
                styleDictionary(getStringValueByKey(context.dataStore,"style_choice"))
            }
        }
        Column(modifier=Modifier.background(styleChoice.value.backgroundColor).fillMaxSize()) {
            HeaderText(styleChoice.value)
            ProfileCardUI(navController,styleChoice.value)
            GeneralOptionsUI(navController,styleChoice.value)
            SupportOptionsUI(navController,styleChoice.value)
        }
}


@Composable
fun HeaderText(styleChoice:Style) {
    Text(
        text = "Settings",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, bottom = 10.dp),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
        style= MaterialTheme.typography.h5.copy(
            color=styleChoice.textColor
        )
    )
}

@Composable
fun ProfileCardUI(navController: NavController,styleChoice:Style) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp),
        backgroundColor = styleChoice.backgroundColor,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(
                    text = "Check Your Profile",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color=styleChoice.textColor
                )

                Text(
                    text = "_____@email.com",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                Button(
                    modifier = Modifier.padding(top = 10.dp).border(width=2.dp,color=styleChoice.textColor,shape = RoundedCornerShape(18)),
                    onClick = {navController.navigate(Screens.Profile.route)
                              },
                    colors = ButtonDefaults.buttonColors(styleChoice.backgroundColor
                    ),
                    contentPadding = PaddingValues(horizontal = 30.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 2.dp
                    ),
                    //shape = Shapes.medium
                ) {
                    Text(
                        text = "View",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        style=MaterialTheme.typography.body1.copy(
                            color=styleChoice.textColor
                        )

                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.ic_profile_card_image),
                contentDescription = "",
                modifier = Modifier.height(120.dp)
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun GeneralOptionsUI(navController:NavController,styleChoice:Style) {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 10.dp)
    ) {
        Text(
            text = "General",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.h5.copy(
                color=styleChoice.textColor
            )
        )
        Divider(color=Color.Black)
        GeneralSettingItem(
            icon = R.drawable.ic_more,
            mainText = "Text Size",
            subText = "Set the font size for the output text of the Eyespeak application.",
            onClick = {navController.navigate(Screens.Customization.route)},
            styleChoice=styleChoice
        )
        GeneralSettingItem(
            icon = R.drawable.ic_more,
            mainText = "Languages",
            subText = "Set languages for translating what Eyespeak reads.",
            onClick = {navController.navigate(Screens.Languages.route)},
            styleChoice=styleChoice
        )
        GeneralSettingItem(
            icon=R.drawable.ic_more,
            mainText="Styling",
            subText = "Set dark, light, and color blind-friendly styles.",
            onClick={navController.navigate(Screens.Styles.route)},
            styleChoice=styleChoice
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun GeneralSettingItem(icon: Int, mainText: String, subText: String, onClick: () -> Unit,styleChoice:Style) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .background(styleChoice.backgroundColor),
        elevation = 0.dp,
    ) {
            Row(
                modifier = Modifier.background(styleChoice.backgroundColor),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,modifier=Modifier.background(styleChoice.backgroundColor)) {
                    Box(
                        modifier = Modifier
                            .size(34.dp).background(styleChoice.backgroundColor)
                        //.clip(shape = Shapes.medium)
                        //.background(LightPrimaryColor)
                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))
                    Column(
                        modifier = Modifier.offset(y = (2).dp)
                            .background(styleChoice.backgroundColor)
                    ) {
                        Text(
                            text = mainText,
                            //fontFamily = Poppins,
                            color = styleChoice.textColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Text(
                            text = subText,
                            //fontFamily = Poppins,
                            color = styleChoice.textColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.offset(y = (-4).dp)
                        )
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_right_arrow),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )

            }
    }
}

@ExperimentalMaterialApi
@Composable
fun SupportOptionsUI(navController:NavController,styleChoice:Style) {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 10.dp)
    ) {
        Text(
            text = "Support",
            //fontFamily = Poppins,
            //color = SecondaryColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.body1.copy(
                color=styleChoice.textColor
            )
        )
        Divider(color=styleChoice.textColor)
        SupportItem(
            icon = R.drawable.ic_feedback,
            mainText = "Feedback",
            onClick = {},
            styleChoice = styleChoice
        )
        SupportItem(
            icon = R.drawable.ic_privacy_policy,
            mainText = "Privacy Policy",
            onClick = {},
            styleChoice = styleChoice
        )
        SupportItem(
            icon = R.drawable.ic_about,
            mainText = "About",
            onClick = {},
            styleChoice = styleChoice
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun SupportItem(icon: Int, mainText: String, onClick: () -> Unit,styleChoice:Style) {
    Card(
        onClick = { onClick() },
        backgroundColor = styleChoice.backgroundColor,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        //.clip(shape = Shapes.medium)
                        //.background(LightPrimaryColor)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = mainText,
//                    fontFamily = Poppins,
//                    color = SecondaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style=MaterialTheme.typography.body1.copy(
                        color=styleChoice.textColor
                    )
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                tint = styleChoice.textColor
            )

        }
    }
}



//@Composable
//private fun MyUI() {
//
//    var sliderValue by remember {
//        mutableStateOf(0f)
//    }
//
//    Slider(
//        modifier = Modifier
//            .width(width = 130.dp)
//            .rotate(degrees = -90f),
//        value = sliderValue,
//        onValueChange = { sliderValue_ ->
//            sliderValue = sliderValue_
//        },
//        onValueChangeFinished = {
//            // this is called when the user completed selecting the value
//            Log.d("SettingsScreen", "sliderValue = $sliderValue")
//        },
//        valueRange = 0f..5f
//    )
//}
