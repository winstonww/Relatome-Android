package com.example.relatome.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.relatome.domain.*

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

@Entity
data class RelationshipEntity(
    @PrimaryKey
    val id: String,
    val as1Name: String,
    val as2Name: String,
    val relationship: String
)

@Entity
data class PendingRelationshipEntity(
    @PrimaryKey
    val id: String, // relationship id
    val as1Name: String,
    val as2Name: String,
    val postedAt: String
)

fun List<PendingRelationshipEntity>.asPendingRelationshipDomainContribute():
        List<PendingRelationshipDomainContribute> {
    return map {
        PendingRelationshipDomainContribute(
            id = it.id,
            as1Name = it.as1Name,
            as2Name = it.as2Name,
            postedAt = it.postedAt
        )
    }
}


fun List<RelationshipEntity>.asRelationshipDomainHome(): List<RelationshipDomainHome> {
    return map {
        RelationshipDomainHome(
            id = it.id,
            as1Name = it.as1Name,
            as2Name = it.as2Name,
            relationship = it.relationship
        )
    }
}


@Entity
data class RelationshipResponseEntity(
    @PrimaryKey
    val relationshipId: String,
    val as1Name: String,
    val as2Name: String,
    val relationship: String
)

fun List<RelationshipResponseEntity>.asRelationshipResponseDomainRevise():
        List<RelationshipResponseDomainRevise> {
    return map {
        RelationshipResponseDomainRevise(
            relationshipId = it.relationshipId,
            as1Name = it.as1Name,
            as2Name = it.as2Name,
            relationship = it.relationship
        )
    }
}