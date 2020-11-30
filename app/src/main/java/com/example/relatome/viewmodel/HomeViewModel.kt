package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.LoginRepository
import kotlinx.coroutines.launch

enum class HomeStatus{
    NOOP,
    LOADING
}
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val loginRepo = LoginRepository(application.applicationContext,
        getDatabase(application.applicationContext)
    )
    val login = loginRepo.loginDomainHome

    private var _loadingStatus = MutableLiveData<HomeStatus>()
    val loadingStatus : LiveData<HomeStatus>
        get() = _loadingStatus

    init {
        viewModelScope.launch {
            _loadingStatus.value = HomeStatus.LOADING


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
