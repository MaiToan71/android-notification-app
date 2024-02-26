package com.example.devicecontrols.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicecontrols.network.ApiProvider
import com.example.devicecontrols.utils.ApiStatus
import com.example.devicecontrols.utils.containsNumberAndVnd
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun postLogsAccountBalance(message: String) {
        if (message.isNotEmpty() && containsNumberAndVnd(message)) {
            viewModelScope.launch {
                _status.value = ApiStatus.LOADING
                try {
                    val isSuccess = ApiProvider.retrofitService.postLogsAccountBalance(message)
                    _status.value = ApiStatus.DONE
                    _message.value = "Success: $isSuccess"
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    _message.value = "Failure: ${e.message}"
                }
                Log.d("Response API", "$_message\nMessage: $message")
            }
        }
    }
}