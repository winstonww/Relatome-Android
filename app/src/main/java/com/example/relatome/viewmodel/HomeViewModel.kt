package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.LoginRepository
import com.example.relatome.repo.RelationshipRepository
import kotlinx.coroutines.launch
import timber.log.Timber

enum class HomeStatus{
    NOOP,
    LOADING
}
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val loginRepo = LoginRepository(getDatabase(application.applicationContext))
    val relationshipRepo = RelationshipRepository(getDatabase(application.applicationContext))

    val login = loginRepo.loginDomainHome

    val relationshipList = relationshipRepo.relationshipList

    private var _loadingStatus = MutableLiveData<HomeStatus>()
    val loadingStatus : LiveData<HomeStatus>
        get() = _loadingStatus

    init {
        refreshRelationships()
    }
    fun refreshRelationships() {
        viewModelScope.launch {
            _loadingStatus.value = HomeStatus.LOADING
            val authToken = loginRepo.getAuthToken()
            Timber.i("Auth Token: ${authToken}")
            relationshipRepo.refreshRelationships(authToken)
            _loadingStatus.value = HomeStatus.NOOP
        }

    }
    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
