package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.LoginRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

enum class LoginStatus {
    NOOP,
    LOGGED_IN,
    ERROR,
}

enum class LoginLoadingStatus {
    NOOP,
    LOADING,
    TIMEOUT,
    NO_CONNECTION
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val loginRepo = LoginRepository(getDatabase(application.applicationContext))

    private var _navigateToLoginStatus = MutableLiveData<LoginStatus>()
    val navigateToLoginStatus : LiveData<LoginStatus>
        get() = _navigateToLoginStatus

    private var _loadingStatus = MutableLiveData<LoginLoadingStatus>()
    val loadingStatus : LiveData<LoginLoadingStatus>
        get() = _loadingStatus

    var job : Job? = null
    init {
        _navigateToLoginStatus.value = LoginStatus.NOOP
        _loadingStatus.value = LoginLoadingStatus.NOOP
    }

    fun login(email: String, password: String) {
        job?.let {
            it.cancel()
        }
        job = viewModelScope.launch {
            _loadingStatus.value = LoginLoadingStatus.LOADING
            Timber.i("in navigate Login. Email ${email}, password: $password")
            try {
                loginRepo.login(email, password)

                _navigateToLoginStatus.value = LoginStatus.LOGGED_IN
                _loadingStatus.value = LoginLoadingStatus.NOOP

            } catch (e: HttpException) {
                if (e.code() == 400) {
                    _navigateToLoginStatus.value = LoginStatus.ERROR
                    _loadingStatus.value = LoginLoadingStatus.NOOP
                    return@launch
                } else {
                    throw e
                }
            } catch (e: java.net.SocketTimeoutException) {
                _loadingStatus.value = LoginLoadingStatus.TIMEOUT
            } catch (e: java.net.UnknownHostException) {
                _loadingStatus.value = LoginLoadingStatus.NO_CONNECTION
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