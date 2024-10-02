package com.devmoss.kabare.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {

    // LiveData to hold the state of the welcome process
    private val _isCompleted = MutableLiveData<Boolean>(false)
    val isCompleted: LiveData<Boolean> get() = _isCompleted

    // Function to mark the welcome process as completed
    fun completeWelcome() {
        _isCompleted.value = true
    }

    // Optional: Function to reset the state if needed
    fun resetWelcome() {
        _isCompleted.value = false
    }
}
