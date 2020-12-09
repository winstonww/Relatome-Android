package com.example.relatome.repo

import androidx.lifecycle.Transformations
import com.example.relatome.database.RelatomeDatabase
import com.example.relatome.database.asRelationshipResponseDomainRevise
import com.example.relatome.network.RelatomeApi
import com.example.relatome.network.asRelationshipResponseEntities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RelationshipResponseRepository(val database: RelatomeDatabase) {

    val relationshipResponseList = Transformations.map(
        database.relationshipResponseDao.getRelationshipResponse()) {
        it.asRelationshipResponseDomainRevise()
    }

    suspend fun refreshRelationshipResponse(authToken: String) {
        withContext(Dispatchers.IO) {
            database.relationshipResponseDao.clearAll()
            database.relationshipResponseDao.insertRelationshipResponses(
                *RelatomeApi.retrofitService.getRelationshipResponses(authToken).asRelationshipResponseEntities()
            )
        }
    }
}