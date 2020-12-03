package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.domain.RelationshipDomainHome
import com.example.relatome.repo.LoginRepository
import com.example.relatome.repo.RelationshipRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

enum class HomeStatus{
    NOOP,
    LOADING,
    TIMEOUT,
    NO_CONNECTION
}
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val loginRepo = LoginRepository(getDatabase(application.applicationContext))
    val relationshipRepo = RelationshipRepository(getDatabase(application.applicationContext))

    val login = loginRepo.loginDomainHome

    var relationshipList = relationshipRepo.relationshipList

    var job : Job? = null
    private var _loadingStatus = MutableLiveData<HomeStatus>()
    val loadingStatus : LiveData<HomeStatus>
        get() = _loadingStatus

    init {
        refreshRelationships()
    }
    fun refreshRelationships() {
        job?.run {
            cancel()
        }
        job = viewModelScope.launch {
            _loadingStatus.value = HomeStatus.LOADING
            val authToken = loginRepo.getAuthToken()
            Timber.i("Auth Token: ${authToken}")
            try {
                relationshipRepo.refreshRelationships(authToken)
            } catch (e: java.net.SocketTimeoutException) {
                _loadingStatus.value = HomeStatus.TIMEOUT
            } catch (e: java.net.UnknownHostException) {
                _loadingStatus.value = HomeStatus.NO_CONNECTION
            }
            _loadingStatus.value = HomeStatus.NOOP
        }

    }

    fun deleteRelationship(item: RelationshipDomainHome?) {
        item?.run {
            job?.run {
                cancel()
            }
            job = viewModelScope.launch {
                val authToken = loginRepo.getAuthToken()
                relationshipRepo.deleteRelationship(authToken, item.id)
                relationshipRepo.refreshRelationships(authToken)
            }
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
