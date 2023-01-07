package com.rmarinov.tempcheck.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.messaging.FirebaseMessaging
import com.rmarinov.tempcheck.data.api.TempService
import com.rmarinov.tempcheck.data.model.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine

@Singleton
class TempRepository @Inject constructor(
    private val tempService: TempService,
    private val dataStore: DataStore<Preferences>
) {
    private suspend fun getToken(): String? =
        dataStore.data.map { it[KEY_TOKEN] }.firstOrNull()

    private suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
        }
    }

    private suspend fun getAuthHeader(): String =
        "Bearer ${getToken()}"

    suspend fun isLoggedIn(): Boolean = getToken() != null

    suspend fun register(request: RegisterRequest) {
        val response = tempService.register(request)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    suspend fun login(username: String, password: String) {
        tempService.login(username, password)
            .also { setToken(it.accessToken) }
    }

    suspend fun getCurrentData(): SensorDataResponse =
        tempService.getCurrentData()

    suspend fun getDataHistory(period: Period): List<SensorDataHistoryResponse> =
        tempService.getDataHistory(period)

    suspend fun getAlerts(): List<AlertResponse> =
        tempService.getAlerts(getAuthHeader())

    suspend fun createAlert(alert: AlertRequest): AlertResponse =
        tempService.createAlert(getAuthHeader(), alert)

    suspend fun changeAlertState(
        alertId: Int,
        modifyAlertRequest: ModifyAlertRequest
    ): AlertResponse = tempService.changeAlertState(getAuthHeader(), alertId, modifyAlertRequest)

    suspend fun deleteAlert(alertId: Int) {
        val response = tempService.deleteAlert(getAuthHeader(), alertId)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    suspend fun sendToken() {
        tempService.sendToken(
            getAuthHeader(),
            TokenRequest(getFirebaseToken())
        )
    }

    private suspend fun getFirebaseToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { continuation.resumeWith(Result.success(it)) }
            .addOnFailureListener { continuation.resumeWith(Result.failure(it)) }
    }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey(name = "token")
    }
}