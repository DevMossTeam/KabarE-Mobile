package com.devmoss.kabare.ui.auth.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmoss.kabare.data.api.ApiConfig
import com.pakelcomedy.kabare.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpInputViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("SignUpPrefs", Context.MODE_PRIVATE)

    private val _firebaseRegistrationResult = MutableLiveData<String>()
    val firebaseRegistrationResult: LiveData<String> get() = _firebaseRegistrationResult

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun savePassword(newPassword: String, confirmPassword: String) {
        if (newPassword != confirmPassword) {
            _firebaseRegistrationResult.value = "Passwords do not match"
            return
        }

        // Retrieve user data from SharedPreferences
        val fullName = sharedPreferences.getString("full_name", null)
        val username = sharedPreferences.getString("username", null)
        val email = sharedPreferences.getString("email", null)

        if (fullName.isNullOrBlank() || username.isNullOrBlank() || email.isNullOrBlank()) {
            _firebaseRegistrationResult.value = "Incomplete user data."
            return
        }

        // Handle user deletion and registration sequentially
        viewModelScope.launch {
            val deletionSuccess = deleteExistingAccount(email)
            if (deletionSuccess) {
                registerInFirebase(email, newPassword, username, fullName)
            }
        }
    }

    private suspend fun deleteExistingAccount(email: String): Boolean {
        return try {
            // Find user by email
            val user = firebaseAuth.currentUser
            if (user != null && user.email == email) {
                // If the currently logged-in user's email matches, delete the account
                user.delete().await()
                Log.d("SignUpInputViewModel", "Existing account deleted successfully.")
                true
            } else {
                Log.d("SignUpInputViewModel", "No account found to delete.")
                true // No account found is considered successful
            }
        } catch (e: Exception) {
            Log.e("SignUpInputViewModel", "Error deleting existing account: ${e.localizedMessage}")
            false
        }
    }

    private fun registerInFirebase(
        email: String,
        password: String,
        username: String,
        fullName: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    if (uid != null) {
                        // Push user data to the database
                        pushUserDataToDatabase(uid, username, fullName, email, password)
                    } else {
                        _firebaseRegistrationResult.postValue("Firebase UID not found.")
                    }
                } else {
                    _firebaseRegistrationResult.postValue(
                        task.exception?.localizedMessage ?: "Firebase registration failed."
                    )
                }
            }
    }

    private fun pushUserDataToDatabase(
        uid: String,
        username: String,
        fullName: String,
        email: String,
        password: String
    ) {
        val apiService = ApiConfig.getApiService()

        val user = User(
            uid = uid,
            nama_pengguna = username,
            nama_lengkap = fullName,
            email = email,
            password = password,
            role = "pembaca" // Default role
        )

        apiService.createUser(user).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("SignUpInputViewModel", "User registration successful: ${response.message()}")
                    _firebaseRegistrationResult.postValue("Registration successful!")
                } else {
                    Log.e("SignUpInputViewModel", "Failed to save user data: ${response.message()}")
                    response.errorBody()?.let { Log.e("SignUpInputViewModel", "Error body: ${it.string()}") }
                    _firebaseRegistrationResult.postValue("Failed to save data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("SignUpInputViewModel", "API call failed: ${t.localizedMessage}", t)
                _firebaseRegistrationResult.postValue("Error: ${t.localizedMessage}")
            }
        })
    }
}