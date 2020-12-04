package com.example.relatome.viewmodel


import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.relatome.database.getDatabase
import com.example.relatome.domain.AsNameDomainAsNameSuggestion
import com.example.relatome.repo.AsNameRepository
import com.example.relatome.repo.LoginRepository
import com.example.relatome.repo.RelationshipRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

enum class AsLoadingStatus {
    NOOP,
    TIMEOUT,
    LOADING,
}

abstract class AsNameViewModel(application: Application, val searchString: String) :
    AndroidViewModel(application) {

    companion object {
        final val AS1ID = "as1Id"
        final val AS2ID = "as2Id"
    }

    val loginRepo = LoginRepository(getDatabase(application.applicationContext))
    val asNameRepo = AsNameRepository(getDatabase(application.applicationContext))
    val relationshipRepo = RelationshipRepository(getDatabase(application.applicationContext))

    var selectedPosition = -1
    private var _asNameList = MutableLiveData<List<AsNameDomainAsNameSuggestion>>()
    val asNameList: LiveData<List<AsNameDomainAsNameSuggestion>>
        get() = _asNameList


    private lateinit var authToken: String

    private var _loadingStatus = MutableLiveData<AsLoadingStatus>()
    val loadingStatus : LiveData<AsLoadingStatus>
        get() = _loadingStatus

    init {
        viewModelScope.launch {
            authToken = loginRepo.getAuthToken()
            refreshAsNames(searchString)

        }
    }
    private var job : Job? = null

    fun refreshAsNames(pattern: String) {
        job?.let {
            it.cancel()
        }
        job = viewModelScope.launch {
            _loadingStatus.value = AsLoadingStatus.LOADING
            try {
                _asNameList.value = asNameRepo.refreshAsNames(authToken, pattern)
                _loadingStatus.value = AsLoadingStatus.NOOP
            } catch (e: java.net.SocketTimeoutException) {
                _loadingStatus.value = AsLoadingStatus.TIMEOUT
            }
        }
    }

    fun addRelationship(sp: SharedPreferences) {

            val as1Id = sp.getString(AS1ID, null)
            val as2Id = sp.getString(AS2ID, null)
            Timber.i("as1id : ${as1Id}")
            Timber.i("as2id : ${as2Id}")
        viewModelScope.launch {
            relationshipRepo.addRelationship(as1Id!!, as2Id!!, authToken)

        }
    }

    abstract fun saveAsId(id: String, sp: SharedPreferences)

}

class As1NameViewModel(application: Application, searchString: String) :
    AsNameViewModel(application, searchString) {

    override fun saveAsId(id: String, sp: SharedPreferences) {
        with(sp.edit()) {
            putString(AS1ID, id)
            apply()
            Timber.i("Saved As1 ID with key $AS1ID and value $id")
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

class As2NameViewModel(application: Application, searchString: String) :
    AsNameViewModel(application, searchString) {

    override fun saveAsId(id: String, sp: SharedPreferences) {
        with(sp.edit()) {
            putString(AS2ID, id)
            apply()
            Timber.i("Saved As2 ID with key $AS2ID and value $id")
        }
    }

    /**
     * Factory for constructing ViewModel with parameter
     */
    class Factory(val app: Application, val pat: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(As2NameViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return As2NameViewModel(app, pat) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}