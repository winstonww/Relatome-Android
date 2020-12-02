package com.example.relatome.repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Transformations
import androidx.room.RoomDatabase
import com.example.relatome.database.RelatomeDatabase
import com.example.relatome.database.asLoginDomainHome
import com.example.relatome.network.LoginRequest
import com.example.relatome.network.LoginResponse
import com.example.relatome.network.RelatomeApi
import com.example.relatome.network.asDatabaseLoginEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository(val database: RelatomeDatabase) {

    val loginDomainHome = Transformations.map(database.loginDao.getLoginEntity()) {
        it.first().asLoginDomainHome()
    }
    suspend fun login(email: String, password: String): Boolean {
        var result: LoginResponse? = null
        withContext(Dispatchers.IO) {
            result = RelatomeApi.retrofitService.login(LoginRequest(email, password))
            database.loginDao.insertLoginEntity(result!!.asDatabaseLoginEntity())
        }
        return result != null
    }

    suspend fun getAuthToken(): String {
        return withContext(Dispatchers.IO) {
            database.loginDao.getDeadLoginEntity().first().authToken
        }
    }
}