package com.example.relatome.network

import com.example.relatome.database.LoginEntity

data class LoginResponse(val name: String, val email: String, val authToken: String)

fun LoginResponse.asDatabaseLoginEntity(id: Long = 0) : LoginEntity {
    return LoginEntity(
        id = id,
        name = name,
        email = email,
        authToken = authToken
    )
}