package com.example.relatome.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.relatome.database.getDatabase
import com.example.relatome.repo.LoginRepository

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

    }
}
