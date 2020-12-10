package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.AddAsRepository
import com.example.relatome.repo.LoginRepository
import com.example.relatome.utils.LoadingStatus
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber



open class AddAsViewModel(application: Application): AndroidViewModel(application) {

    val addAsRepo = AddAsRepository()

    val loginRepo = LoginRepository(getDatabase(application.applicationContext))

    private var _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus : LiveData<LoadingStatus>
        get() = _loadingStatus

    private var _navigateToAsSuggestion = MutableLiveData<Boolean>()
    val navigateToAsSuggestion : LiveData<Boolean>
        get() = _navigateToAsSuggestion

    init {
        _navigateToAsSuggestion.value = false
        _loadingStatus.value = LoadingStatus.Noop()
    }


    fun addAnatomicalStructure(name: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.Loading()
            try {
                addAsRepo.addAnatomicalStructure(loginRepo.getAuthToken(), name)
                _loadingStatus.value = LoadingStatus.Noop()
                _navigateToAsSuggestion.value = true
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    Timber.i("400 error")
                    _loadingStatus.value = LoadingStatus.Error("Anatomical Structure already exists")
                }
            } catch (e: java.net.SocketTimeoutException) {
                _loadingStatus.value = LoadingStatus.Timeout()
            } catch (e: java.net.UnknownHostException) {
                _loadingStatus.value = LoadingStatus.NoConnection()
            }

        }
    }

    fun navigateToAsSuggestionComplete() {
        _navigateToAsSuggestion.value = false
    }
}

class AddAs1ViewModel(application: Application) : AddAsViewModel(application) {
    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddAs1ViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddAs1ViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

class AddAs2ViewModel(application: Application) : AddAsViewModel(application) {
    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddAs2ViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddAs2ViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}