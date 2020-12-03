package com.example.relatome.repo

import androidx.lifecycle.Transformations
import com.example.relatome.database.RelatomeDatabase
import com.example.relatome.database.asRelationshipDomainHome
import com.example.relatome.network.AddRelationshipRequest
import com.example.relatome.network.DeleteRelationshipRequest
import com.example.relatome.network.RelatomeApi
import com.example.relatome.network.asDatabaseRelationshipEntities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RelationshipRepository(private val database: RelatomeDatabase) {

    val relationshipList = Transformations.map(database.relationshipDao.getRelationships()) {
        it.asRelationshipDomainHome()
    }

    suspend fun refreshRelationships(authToken: String) {
        withContext(Dispatchers.IO) {
            val relationships = RelatomeApi.retrofitService.getRelationships(authToken)
            database.relationshipDao.clearAll()
            database.relationshipDao.insertRelationships(*relationships.asDatabaseRelationshipEntities())
        }
    }

    suspend fun addRelationship(as1Id: String, as2Id: String, authToken: String) {
        withContext(Dispatchers.IO) {
            RelatomeApi.retrofitService.addRelationship(authToken, AddRelationshipRequest(as1Id, as2Id))
        }
    }

    suspend fun deleteRelationship(authToken: String, relationshipId: String) {
        RelatomeApi.retrofitService.deleteRelationship(authToken, DeleteRelationshipRequest(relationshipId))
    }
}