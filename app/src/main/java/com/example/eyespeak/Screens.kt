package com.example.eyespeak

sealed class Screens(val route:String)
{
    object Landing: Screens("landing_screen")
    object Login: Screens("login_screen")
    object Home: Screens("home_screen")

    object Register: Screens("register_screen")

    object Profile: Screens("profile_screen")

    object Settings: Screens("settings_screen")

    object Introduction: Screens("introduction_screen")
}