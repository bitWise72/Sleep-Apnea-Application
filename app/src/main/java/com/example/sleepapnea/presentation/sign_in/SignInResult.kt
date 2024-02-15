package com.example.sleepapnea.presentation.sign_in


data class SignInResult(
    val userData: UserData?,
    val errorMessage: String?)

data class UserData(
    private val userId:String,
    private val userName:String?,
    private val profilePicUrl:String?
)