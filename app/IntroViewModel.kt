package com.devmoss.kabare.ui.intro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmoss.kabare.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager: SessionManager = SessionManager(application) // SessionManager instance

    private val _introStatus = MutableLiveData<IntroStatus>()
    val introStatus: LiveData<IntroStatus> get() = _introStatus

    init {
        startIntro()
    }

    private fun startIntro() {
        _introStatus.value = IntroStatus.Loading

        viewModelScope.launch {
            delay(500) // Simulate loading delay
            if (sessionManager.isLoggedIn()) {
                // If user is logged in, navigate directly to Home
                _introStatus.value = IntroStatus.NavigateToHome
            } else {
                // If user is not logged in, show the intro and navigate to Welcome
                _introStatus.value = IntroStatus.ShowIntro
                delay(500) // Optional delay before navigating
                proceedToWelcome()
            }
        }
    }

    private fun proceedToWelcome() {
        _introStatus.value = IntroStatus.NavigateToWelcome
    }

    // Sealed class representing different states of the intro
    sealed class IntroStatus {
        object Loading : IntroStatus()
        object ShowIntro : IntroStatus()
        object NavigateToWelcome : IntroStatus()
        object NavigateToHome : IntroStatus() // New state for navigating directly to Home
    }
}
