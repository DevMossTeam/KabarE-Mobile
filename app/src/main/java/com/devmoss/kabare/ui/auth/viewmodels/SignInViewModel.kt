package com.example.yourapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    // LiveData for username and password
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    // LiveData for authentication result
    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> get() = _isAuthenticated

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun setUsername(username: String) {
        _username.value = username
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun performSignIn() {
        // Simple validation
        if (_username.value.isNullOrEmpty()) {
            _errorMessage.value = "Username cannot be empty"
            return
        }

        if (_password.value.isNullOrEmpty()) {
            _errorMessage.value = "Password cannot be empty"
            return
        }

        // Perform sign-in logic
        viewModelScope.launch {
            authenticate(_username.value!!, _password.value!!)
        }
    }

    private suspend fun authenticate(username: String, password: String) {
        // Simulate a network call (replace this with your actual authentication logic)
        // Here, we just check against hardcoded values for demonstration purposes
        if (username == "testuser" && password == "password") {
            _isAuthenticated.value = true
            _errorMessage.value = null // Clear any previous errors
        } else {
            _isAuthenticated.value = false
            _errorMessage.value = "Invalid Username or Password"
        }
    }
}
