package com.devmoss.kabare.data.model

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("new_password")
    val newPassword: String
)