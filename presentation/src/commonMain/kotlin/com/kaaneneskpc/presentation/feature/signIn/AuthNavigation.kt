package com.kaaneneskpc.presentation.feature.signIn

sealed class AuthNavigation {
    object ToLogin : AuthNavigation()
    object ToSignUp : AuthNavigation()
    object ToListing : AuthNavigation()
}