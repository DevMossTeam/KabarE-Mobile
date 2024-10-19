package com.devmoss.kabare.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroViewModel : ViewModel() {

    private val _introStatus = MutableLiveData<IntroStatus>()
    val introStatus: LiveData<IntroStatus> get() = _introStatus

    init {
        startIntro()
    }

    private fun startIntro() {
        _introStatus.value = IntroStatus.Loading

        viewModelScope.launch {
            delay(500) // Simulate a delay of 2 seconds
            _introStatus.value = IntroStatus.ShowIntro // Update status to ShowIntro after delay
            delay(500) // Optional: keep showing intro for 2 seconds
            proceedToNext() // Proceed to next state
        }
    }

    // Function to proceed to the Welcome screens
    private fun proceedToNext() {
        _introStatus.value = IntroStatus.NavigateToWelcome // Update status to NavigateToWelcome
    }

    // Sealed class representing different states of the intro
    sealed class IntroStatus {
        object Loading : IntroStatus()
        object ShowIntro : IntroStatus()
        object NavigateToWelcome : IntroStatus() // New state for navigating to welcome
    }
}
