package com.example.relatome.repo

import androidx.lifecycle.Transformations
import com.example.relatome.database.RelatomeDatabase
import com.example.relatome.database.asPendingRelationshipDomainContribute
import com.example.relatome.network.RelatomeApi
import com.example.relatome.network.asPendingRelationshipEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PendingRelationshipRepository(val database: RelatomeDatabase) {

    val pendingRelationshipList = Transformations.map(database.pendingRelationshipDao.getPendingRelationships()) {
            it -> it.asPendingRelationshipDomainContribute()
    }

    suspend fun refreshPendingRelationships(authToken: String) {

        withContext(Dispatchers.IO) {
            database.pendingRelationshipDao.clearAll()
            database.pendingRelationshipDao.insertPendingRelationships(
                *RelatomeApi.retrofitService.getPendingRelationships(authToken)
                    .asPendingRelationshipEntity()
            )
        }
    }
}