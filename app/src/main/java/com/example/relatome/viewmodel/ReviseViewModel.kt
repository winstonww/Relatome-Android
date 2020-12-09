package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.LoginRepository
import com.example.relatome.repo.RelationshipResponseRepository
import kotlinx.coroutines.launch

class ReviseViewModel(application: Application): AndroidViewModel(application) {

    val responseRepo = RelationshipResponseRepository(getDatabase(application))

    val loginRepo = LoginRepository(getDatabase(application))

    val relationshipResponseList = responseRepo.relationshipResponseList
    /**
     * Factory for constructing ViewModel with parameter
     */
    init {
        viewModelScope.launch {

            responseRepo.refreshRelationshipResponse(loginRepo.getAuthToken())
        }
    }
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReviseViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReviseViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}