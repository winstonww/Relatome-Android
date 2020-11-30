package com.example.relatome.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.relatome.domain.LoginDomainHome

@Entity
data class LoginEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo
    val name : String,
    @ColumnInfo
    val email: String,
    @ColumnInfo
    val authToken: String
)

fun LoginEntity.asLoginDomainHome() : LoginDomainHome {
    return LoginDomainHome(
        name = name,
        authToken = authToken
    )
}