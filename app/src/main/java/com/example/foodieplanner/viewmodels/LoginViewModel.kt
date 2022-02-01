package com.example.foodieplanner.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _loginButtonText = MutableLiveData("Sign Up")

    val usernameHint: LiveData<String> = MutableLiveData("Username")
    val passwordHint: LiveData<String> = MutableLiveData("Password")
    val loginButtonText: LiveData<String> = _loginButtonText



}