package com.example.relatome.network

import com.example.relatome.database.LoginEntity
import com.example.relatome.database.RelationshipEntity

data class LoginResponse(val name: String, val email: String, val authToken: String)

fun LoginResponse.asDatabaseLoginEntity(id: Long = 0) : LoginEntity {
    return LoginEntity(
        id = id,
        name = name,
        email = email,
        authToken = authToken
    )
}

data class RelationshipResponse(
    val id: String,
    val as1Name: String,
    val as2Name: String,
    val relationship: String
)

fun List<RelationshipResponse>.asDatabaseRelationshipEntities(id: Long = 0) : Array<RelationshipEntity> {
    return map {
        RelationshipEntity(
            id = it.id,
            as1Name = it.as1Name,
            as2Name = it.as2Name,
            relationship = it.relationship
        )
    }.toTypedArray()
}