package com.devmoss.kabare.ui.auth.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.devmoss.kabare.R
import com.devmoss.kabare.data.api.ApiConfig
import com.devmoss.kabare.data.model.CheckUserRequest
import com.devmoss.kabare.data.model.ResponseCheckUser
import com.devmoss.kabare.data.model.SignInRequest
import com.devmoss.kabare.data.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    val signInStatus = MutableLiveData<String>()
    val showToast = MutableLiveData<String>()
    val navigationAction = MutableLiveData<String>()

    private val apiInterface = ApiConfig.getApiService() // Use ApiConfig to get the API interface
    private val userRepository: UserRepository = UserRepository(application) // Initialize UserRepository

    // SharedPreferences untuk menyimpan data sementara
    private val sharedPreferences = application.getSharedPreferences("SignUpPrefs", Context.MODE_PRIVATE)

    // Validate username and password
    fun validateInputs(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            signInStatus.value = "Username cannot be empty"
            return false
        }
        if (password.isEmpty()) {
            signInStatus.value = "Password cannot be empty"
            return false
        }
        return true
    }

// Sign in using API (username/email and password)
    fun signInWithApi(usernameOrEmail: String, password: String) {
        if (validateInputs(usernameOrEmail, password)) {
            val signInRequest = SignInRequest(username_or_email = usernameOrEmail, password = password)

            // API call to sign in
            apiInterface.signIn(signInRequest).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Assuming the response body contains a user object in the response
                        val userResponse = response.body()?.string() // You might need to parse it as per your response structure

                        // Log or parse the response (this is just an example)
                        val userJson = JSONObject(userResponse) // Use appropriate parsing here, like Gson or Moshi

                        val uid = userJson.getJSONObject("user").getString("uid")

                        // Save UID to UserRepository
                        userRepository.saveUserUid(uid)
                        userRepository.saveLoginStatus(true) // Mark the user as logged in

                        showToastOnMain("Sign In Successful")
                        signInStatus.value = "success"
                    } else {
                        // Handle invalid login
                        showToastOnMain("Invalid Username or Password")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    showToastOnMain("Error: ${t.message}")
                }
            })
        }
    }

    // Sign in with Google (using Firebase Auth, optional)
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        if (account == null) {
            showToastOnMain("Google Sign-In Failed: Account is null")
            return
        }
        Log.d("SignInViewModel", "Authenticating with Firebase using Google account: ${account?.email}")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                Log.d("SignInViewModel", "Firebase Sign-In successful for user: ${firebaseUser?.email}")
                val email = firebaseUser?.email
                Log.d("GoogleSignIn", "Account: $account")

                if (email != null) {
                    // Periksa email di database dan simpan UID
                    checkEmailInDatabase(email, firebaseUser)
                } else {
                    showToastOnMain("Google account email not found")
                }
            } else {
                Log.e("SignInViewModel", "Firebase Sign-In failed: ${task.exception?.message}")
                showToastOnMain("Google Sign-In Failed")
            }
        }
    }

    private fun checkEmailInDatabase(email: String, firebaseUser: FirebaseUser) {
        val request = CheckUserRequest(email = email)
        apiInterface.checkUser(request).enqueue(object : Callback<ResponseCheckUser> {
            override fun onResponse(call: Call<ResponseCheckUser>, response: Response<ResponseCheckUser>) {
                if (response.isSuccessful && response.body()?.exists == true) {
                    // Email ditemukan, navigasi ke IntroFragment
                    navigationAction.value = "navigate_to_intro"
                    // Save user data to UserRepository
                    userRepository.saveUserUid(firebaseUser.uid)
                    userRepository.saveLoginStatus(true)
                } else {
                    // Email tidak ditemukan, lanjutkan dengan pembuatan data sementara
                    prepareUserData(firebaseUser)
                    navigationAction.value = "navigate_to_sign_up_input"
                }
            }

            override fun onFailure(call: Call<ResponseCheckUser>, t: Throwable) {
                showToastOnMain("Error checking email: ${t.message}")
            }
        })
    }

    private fun prepareUserData(firebaseUser: FirebaseUser) {
        val email = firebaseUser.email ?: return
        val uid = firebaseUser.uid
        val fullName = firebaseUser.displayName
        val profilePictureUrl = firebaseUser.photoUrl?.toString()

        val baseUsername = email.substringBefore("@")
        generateUniqueUsername(baseUsername) { username ->
            // Simpan data di SharedPreferences
            sharedPreferences.edit()
                .putString("uid", uid)
                .putString("full_name", fullName)
                .putString("username", username)
                .putString("email", email)
                .putString("profile_picture", profilePictureUrl)
                .apply()

            showToastOnMain("Google account data prepared. Please complete registration.")
        }
    }

    private fun generateUniqueUsername(baseUsername: String, callback: (String) -> Unit) {
        val request = CheckUserRequest(username = baseUsername)
        apiInterface.checkUser(request).enqueue(object : Callback<ResponseCheckUser> {
            override fun onResponse(call: Call<ResponseCheckUser>, response: Response<ResponseCheckUser>) {
                if (response.isSuccessful && response.body()?.exists == false) {
                    callback(baseUsername)
                } else {
                    callback("$baseUsername${(1..1000).random()}")
                }
            }

            override fun onFailure(call: Call<ResponseCheckUser>, t: Throwable) {
                callback("$baseUsername${(1..1000).random()}")
            }
        })
    }

    fun getGoogleSignInOptions(): GoogleSignInOptions {
        val context = getApplication<Application>().applicationContext
        val webClientId = context.getString(R.string.default_web_client_id)
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }

    // Helper function to show Toast on the main thread
    private fun showToastOnMain(message: String) {
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            showToast.value = message
        }
    }
}