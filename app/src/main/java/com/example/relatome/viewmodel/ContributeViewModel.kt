package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.LoginRepository
import com.example.relatome.repo.PendingRelationshipRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ContributeLoadingStatus{
    NOOP,
    LOADING,
    NO_CONNECTION,
    TIMEOUT
}
class ContributeViewModel(application: Application): AndroidViewModel(application) {

    val loginRepo = LoginRepository(
        getDatabase(application.applicationContext))

    val pendingRelationshipRepo = PendingRelationshipRepository(getDatabase(application.applicationContext))
    val pendingRelationshipList = pendingRelationshipRepo.pendingRelationshipList

    private var _loadingStatus = MutableLiveData<ContributeLoadingStatus>()
    val loadingStatus : LiveData<ContributeLoadingStatus>
        get() = _loadingStatus

    var job : Job? = null

    init {
        job?.run {
            cancel()
        }
        job = viewModelScope.launch {
            _loadingStatus.value = ContributeLoadingStatus.LOADING
            try {
                refreshPendingRelationship()
                _loadingStatus.value = ContributeLoadingStatus.NOOP
            } catch (e: java.net.SocketTimeoutException) {
                _loadingStatus.value = ContributeLoadingStatus.TIMEOUT
            } catch (e: java.net.UnknownHostException) {
                _loadingStatus.value = ContributeLoadingStatus.NO_CONNECTION
            }
        }

    }

    suspend fun refreshPendingRelationship() {
        pendingRelationshipRepo.refreshPendingRelationships(loginRepo.getAuthToken())
    }
    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContributeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ContributeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}