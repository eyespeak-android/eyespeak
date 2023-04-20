package com.example.eyespeak

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NavGraph (context:MainActivity,modifier: Modifier= Modifier, navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController=navController,
        startDestination=Screens.Landing.route
    )
    {
        composable(Screens.Landing.route)
        {
            LandingScreen(navController)
        }
        composable(Screens.Login.route)
        {
            LoginScreen(navController)
        }
        composable(Screens.Home.route)
        {
            HomeScreen(navController,context)
        }
        composable(Screens.Register.route)
        {
            RegisterScreen(navController)
        }
        composable(Screens.Introduction.route)
        {
            IntroductionScreen(navController)
        }
        composable(Screens.Profile.route)
        {
            ProfileScreen(navController)
        }
        composable(Screens.Settings.route)
        {
            SettingsScreen(navController)
        }

        composable(Screens.Customization.route)
        {TextScreen(navController)
        }

//        composable(Screens.Languages.route)
//        {LanguagesScreen(navController)
//        }
    }
}