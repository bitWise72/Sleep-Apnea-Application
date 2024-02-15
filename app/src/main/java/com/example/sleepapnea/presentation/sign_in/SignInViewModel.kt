package com.example.sleepapnea.presentation.sign_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sleepapnea.SleepSessionViewModel

class SignInViewModel : ViewModel() {

    private var state= MutableLiveData<SignInstate>()

    fun getState(user: SignInResult?): MutableLiveData<SignInstate>{
        state.postValue(SignInstate(
            isSignedInSuccessful = user?.userData != null,
            errorMessage = user?.errorMessage
        ))

        return state
    }

    fun resetState(){
        state.postValue(SignInstate())
    }
}

class SignInViewModelFactory : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)){
            return SignInViewModel() as T
        }
        throw IllegalArgumentException("UNKNOWN CLASS")
    }
}