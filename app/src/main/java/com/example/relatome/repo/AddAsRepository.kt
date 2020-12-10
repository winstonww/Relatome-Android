package com.example.relatome.repo

import com.example.relatome.network.AddAsRequest
import com.example.relatome.network.RelatomeApi

class AddAsRepository {

    suspend fun addAnatomicalStructure(authToken : String, name: String) {
        RelatomeApi.retrofitService.addAnatomicalStructure(authToken, AddAsRequest(name))
    }
}