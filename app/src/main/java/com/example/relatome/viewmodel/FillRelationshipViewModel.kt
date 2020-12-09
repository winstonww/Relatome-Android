package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.FillRelationshipRepository
import com.example.relatome.repo.LoginRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FillRelationshipViewModel(application: Application) : AndroidViewModel(application) {
    val fillRelationshipRepo = FillRelationshipRepository()
    val loginRepo = LoginRepository(getDatabase(application.applicationContext))

    private var _navigateToContributeBN = MutableLiveData<Boolean>()
    val navigateToContributeBN : LiveData<Boolean>
        get() = _navigateToContributeBN

    init {
        _navigateToContributeBN.value = false
    }

    var job: Job? = null
    fun fillRelationship(relationship: String, relationshipId: String) {
        job?.run {
            cancel()
        }
        job = viewModelScope.launch {
            fillRelationshipRepo.fillRelationship(
                loginRepo.getAuthToken(),
                relationship, relationshipId)
            _navigateToContributeBN.value = true
        }
    }

    fun navgiateToContributeBNComplete() {
        _navigateToContributeBN.value = false
    }
    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FillRelationshipViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FillRelationshipViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}