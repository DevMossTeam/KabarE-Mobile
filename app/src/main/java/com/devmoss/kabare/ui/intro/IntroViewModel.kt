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
        // Set initial status to Loading
        _introStatus.value = IntroStatus.Loading

        // Launch a coroutine to simulate delay
        viewModelScope.launch {
            delay(2000) // Simulate a delay of 2 seconds
            _introStatus.value = IntroStatus.ShowIntro // Update status to ShowIntro after delay
        }
    }

    fun proceedToNext() {
        _introStatus.value = IntroStatus.Complete
    }

    sealed class IntroStatus {
        object Loading : IntroStatus()
        object ShowIntro : IntroStatus()
        object Complete : IntroStatus()
    }
}