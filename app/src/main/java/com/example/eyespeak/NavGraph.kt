package com.example.eyespeak

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost

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

        composable(Screens.Profile.route)
        {
            ProfileScreen(navController)
        }
    }
}