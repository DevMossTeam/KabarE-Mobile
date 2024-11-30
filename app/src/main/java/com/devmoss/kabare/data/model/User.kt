package com.pakelcomedy.kabare.model

data class User(
    val email: String,                    // User's email (non-nullable)
    val nama_lengkap: String,             // Full name (non-nullable)
    val nama_pengguna: String,            // Username (non-nullable)
    val uid: String,                      // User ID (non-nullable)
    val role: String,                     // User role (non-nullable)
    val password: String,                 // Password (non-nullable)
    val profile_pic: String? = null,      // Profile picture URL, nullable
    val kredensial: String? = null        // Credentials, nullable
)