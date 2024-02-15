package com.example.sleepapnea.presentation.sign_in

data class SignInstate(
    val isSignedInSuccessful: Boolean = false,
    val errorMessage: String? = null
)
