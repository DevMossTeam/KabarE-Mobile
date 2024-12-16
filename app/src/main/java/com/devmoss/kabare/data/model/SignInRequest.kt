package com.devmoss.kabare.data.model

data class SignInRequest(
    val username_or_email: String,
    val password: String
)