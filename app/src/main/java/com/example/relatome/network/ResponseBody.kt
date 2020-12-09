package com.example.relatome.network

import com.example.relatome.database.LoginEntity
import com.example.relatome.database.PendingRelationshipEntity
import com.example.relatome.database.RelationshipEntity
import com.example.relatome.database.RelationshipResponseEntity
import com.example.relatome.domain.AsNameDomainAsNameSuggestion

data class LoginResponse(val name: String, val email: String, val authToken: String)

fun LoginResponse.asDatabaseLoginEntity(id: Long = 0) : LoginEntity {
    return LoginEntity(
        id = id,
        name = name,
        email = email,
        authToken = authToken
    )
}

data class AddRelationshipResponse(
    val _id: String
)

data class RelationshipResponse(
    val id: String,
    val as1Name: String,
    val as2Name: String,
    val relationship: String
)

data class DeleteRelationshipResponse(
    val result: String
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

data class AsResponse(
    val _id: String,
    val name: String,
    val createdBy: String
)

fun List<AsResponse>.asAsNameDomainAsNameSuggestion(id: Long = 0) : List<AsNameDomainAsNameSuggestion> {
    return map {
        AsNameDomainAsNameSuggestion(
            id = it._id,
            asName = it.name
        )
    }
}

data class PendingRelationshipResponse(
    val id: String,
    val as1Name: String,
    val as2Name: String,
    val postedAt: String)

fun List<PendingRelationshipResponse>.asPendingRelationshipEntity() : Array<PendingRelationshipEntity> {
    return map {
        PendingRelationshipEntity(
            id = it.id,
            as1Name = it.as1Name,
            as2Name = it.as2Name,
            postedAt = it.postedAt
        )
    }.toTypedArray()
}


data class RelationshipResponseResponse(
    val relationshipId: String,
    val as1Name: String,
    val as2Name: String,
    val relationship: String
)

fun List<RelationshipResponseResponse>.asRelationshipResponseEntities(): Array<RelationshipResponseEntity> {
    return map {
        RelationshipResponseEntity(
            relationshipId = it.relationshipId,
            as1Name = it.as1Name,
            as2Name = it.as2Name,
            relationship = it.relationship
        )
    }.toTypedArray()
}
