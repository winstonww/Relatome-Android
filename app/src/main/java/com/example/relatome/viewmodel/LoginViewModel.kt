package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.LoginRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

enum class LoginStatus {
    NOOP,
    LOADING,
    LOGGED_IN,
    ERROR
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val loginRepo = LoginRepository(getDatabase(application.applicationContext))

    private val _navigateToLoginStatus = MutableLiveData<LoginStatus>()
    val navigateToLoginStatus : LiveData<LoginStatus>
        get() = _navigateToLoginStatus

    init {
        _navigateToLoginStatus.value = LoginStatus.NOOP
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _navigateToLoginStatus.value = LoginStatus.LOADING
            Timber.i("in navigate Login. Email ${email}, password: $password")
            try {
                val success = loginRepo.login(email, password)
                if (success) {
                    _navigateToLoginStatus.value = LoginStatus.LOGGED_IN
                }
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    _navigateToLoginStatus.value = LoginStatus.ERROR
                    return@launch
                } else {
                    throw e
                }
            }

        }
    }

    fun setNavigateToLoginComplete() {
        _navigateToLoginStatus.value = LoginStatus.NOOP
    }
    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}