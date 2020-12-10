package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.FillRelationshipRepository
import com.example.relatome.repo.LoginRepository
import com.example.relatome.utils.LoadingStatus
import kotlinx.coroutines.launch


class FillReviseViewModel(application: Application) : AndroidViewModel(application) {
    val loginRepo = LoginRepository(getDatabase(application.applicationContext))
    val fillRelationshipRepo = FillRelationshipRepository()

    private var _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus : LiveData<LoadingStatus>
        get() = _loadingStatus

    private var _navigate = MutableLiveData<Boolean>()
    val navigate : LiveData<Boolean>
        get() = _navigate

    init {
        _navigate.value = false
        _loadingStatus.value = LoadingStatus.Noop()
    }

    fun reviseRelationship(relationship: String, relationshipId: String) {
        viewModelScope.launch {

            _loadingStatus.value = LoadingStatus.Loading()
            try {
                fillRelationshipRepo.fillRelationship(
                    loginRepo.getAuthToken(),
                    relationship,
                    relationshipId
                )
                _loadingStatus.value = LoadingStatus.Noop()
                _navigate.value = true
            } catch (e: java.net.SocketTimeoutException) {
                _loadingStatus.value = LoadingStatus.Timeout()
            } catch (e: java.net.UnknownHostException) {
                _loadingStatus.value = LoadingStatus.NoConnection()
            }
        }
    }

    fun navigateComplete() {
        _navigate.value = false
    }

    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FillReviseViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FillReviseViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}