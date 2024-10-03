// WelcomeViewModel.kt
package devmoss.kabare.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {

    // MutableLiveData to hold the current page index
    private val _currentPage = MutableLiveData<Int>(0)
    val currentPage: LiveData<Int> get() = _currentPage

    // Function to move to the next page
    fun nextPage() {
        _currentPage.value?.let { currentIndex ->
            // Update to the next page index if it's within range
            _currentPage.value = currentIndex + 1
        }
    }

    // Function to move to the previous page
    fun previousPage() {
        _currentPage.value?.let { currentIndex ->
            // Update to the previous page index if it's greater than zero
            if (currentIndex > 0) {
                _currentPage.value = currentIndex - 1
            }
        }
    }

    // Additional logic can be added here as needed
}