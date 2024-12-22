package com.devmoss.kabare.data.model

data class DeviceTokenRequest(
    val device_token: String,
    val user_id: String? = null // Opsional
)