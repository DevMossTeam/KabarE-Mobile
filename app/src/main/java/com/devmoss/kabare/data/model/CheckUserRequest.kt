package com.devmoss.kabare.data.model

import com.pakelcomedy.kabare.model.User

data class CheckUserRequest(
    val email: String? = null,
    val username: String? = null
)

data class ResponseCheckUser(
    val exists: Boolean,
    val message: String
)
