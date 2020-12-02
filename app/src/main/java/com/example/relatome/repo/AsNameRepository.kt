package com.example.relatome.repo

import com.example.relatome.database.RelatomeDatabase
import com.example.relatome.domain.AsNameDomainAsNameSuggestion
import com.example.relatome.network.RelatomeApi
import com.example.relatome.network.asAsNameDomainAsNameSuggestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsNameRepository(private val database: RelatomeDatabase) {

    suspend fun refreshAsNames(authToken: String, pattern: String): List<AsNameDomainAsNameSuggestion> {
        return withContext(Dispatchers.IO) {
            RelatomeApi.retrofitService.getAs(authToken, pattern).asAsNameDomainAsNameSuggestion()
        }
    }

}