package com.example.relatome.domain

data class LoginDomainHome(
    val name: String,
    val authToken: String
)

data class RelationshipDomainHome(
    val id: String,
    val as1Name: String,
    val as2Name: String,
    val relationship : String
)

data class PendingRelationshipDomainContribute(
    val id: String,
    val as1Name: String,
    val as2Name: String,
    val postedAt: String
)

data class AsNameDomainAsNameSuggestion(
    val id: String,
    val asName: String
)

data class RelationshipResponseDomainRevise(
    val relationshipId: String,
    val as1Name: String,
    val as2Name: String,
    val relationship: String
)