package com.example.relatome.repo

import com.example.relatome.database.RelatomeDatabase
import com.example.relatome.network.FillRelationshipRequest
import com.example.relatome.network.RelatomeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FillRelationshipRepository {


    suspend fun fillRelationship(authToken: String, relationship: String, relationshipId: String) {
        withContext(Dispatchers.IO) {
            RelatomeApi.retrofitService.fillRelationship(authToken,
                FillRelationshipRequest(relationshipId, relationship))
        }
    }

}