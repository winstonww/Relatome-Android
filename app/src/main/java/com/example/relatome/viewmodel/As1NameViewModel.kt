package com.example.relatome.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.domain.AsNameDomainAsNameSuggestion
import com.example.relatome.repo.AsNameRepository
import com.example.relatome.repo.LoginRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class As1NameViewModel(application: Application, val searchString: String ) : AndroidViewModel(application) {
    val loginRepo = LoginRepository(getDatabase(application.applicationContext))

    val asNameRepo = AsNameRepository(getDatabase(application.applicationContext))

    var selectedPosition = -1
    private var _asNameList = MutableLiveData<List<AsNameDomainAsNameSuggestion>>()
    val asNameList : LiveData<List<AsNameDomainAsNameSuggestion>>
        get() = _asNameList

    private lateinit var authToken: String
    init {
        viewModelScope.launch {
            authToken = loginRepo.getAuthToken()
            refreshAsNames(searchString)
        }

    }

    fun refreshAsNames(pattern: String) {
        viewModelScope.launch {
            _asNameList.value = asNameRepo.refreshAsNames(authToken, pattern)
        }
    }

    fun saveAs1Id(id: String, sp: SharedPreferences) {
        viewModelScope.launch {
            with(sp.edit()) {
                putString(id, "as1Id")
                apply()
                Timber.i("Saved As1 ID")
            }
        }
    }

    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application, val pat: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(As1NameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return As1NameViewModel(app, pat) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}