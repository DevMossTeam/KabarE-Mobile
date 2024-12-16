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
            _introStatus.value = IntroStatus.ShowIntro
            delay(100) // Delay 5 detik sebelum melanjutkan
            proceedToNext()
        }
    }

    private fun proceedToNext() {
        _introStatus.value = IntroStatus.NavigateToWelcome
    }

    sealed class IntroStatus {
        object Loading : IntroStatus()
        object ShowIntro : IntroStatus()
        object NavigateToWelcome : IntroStatus()
    }
}