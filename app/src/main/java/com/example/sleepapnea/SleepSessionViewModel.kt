package com.example.sleepapnea

import android.os.RemoteException
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

class SleepSessionViewModel (private val healthConnectManager: HealthConnectManager) :
    ViewModel() {

    val permissions= setOf(HealthPermission.getReadPermission(SleepSessionRecord::class))

    private var permissionsGranted=MutableLiveData<Boolean>(false)

    private var sessionsList=MutableLiveData<List<SleepSessionRecord>>(listOf())

    val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

    private var uiState= MutableLiveData<UiState>(UiState.Uninitialized)

    fun initialLoad() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                readSleepSessions()
            }
        }
    }

    fun getPermissionsGrantedValue(): MutableLiveData<Boolean> {
        viewModelScope.launch {
            permissionsGranted.value=healthConnectManager.hasAllPermissions(permissions)
        }
        return permissionsGranted
    }

    fun getSessionsList(): MutableLiveData<List<SleepSessionRecord>>{ return sessionsList}
    private suspend fun readSleepSessions() {
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val now = Instant.now()

        sessionsList.value = healthConnectManager.readSleepSessions(startOfDay.toInstant(), now)
    }

    private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
        uiState.value = try {
            if (permissionsGranted.value!!) {
                block()
            }
            UiState.Done
        } catch (remoteException: RemoteException) {
            UiState.Error(remoteException)
        } catch (securityException: SecurityException) {
            UiState.Error(securityException)
        } catch (ioException: IOException) {
            UiState.Error(ioException)
        } catch (illegalStateException: IllegalStateException) {
            UiState.Error(illegalStateException)
        }
    }

    sealed class UiState {
        object Uninitialized : UiState()
        object Done : UiState()

        // A random UUID is used in each Error object to allow errors to be uniquely identified,
        // and recomposition won't result in multiple snackbars.
        data class Error(val exception: Throwable, val uuid: UUID = UUID.randomUUID()) : UiState()
    }
}

class SleepSessionViewModelFactory(
    private val healthConnectManager: HealthConnectManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepSessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SleepSessionViewModel(
                healthConnectManager = healthConnectManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}