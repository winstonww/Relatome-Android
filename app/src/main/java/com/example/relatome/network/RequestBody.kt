package com.example.relatome.network

data class LoginRequest(val email: String, val password: String)

data class AddRelationshipRequest(val asId1: String, val asId2: String)

data class DeleteRelationshipRequest(val id: String)

data class FillRelationshipRequest(val relationshipId: String, val relationship: String)

data class AddAsRequest(val name: String)
