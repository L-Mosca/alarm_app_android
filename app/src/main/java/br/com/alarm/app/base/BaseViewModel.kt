package br.com.alarm.app.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    val errorMessage: LiveData<Any>
        get() = mErrorMessage
    protected val mErrorMessage = MutableLiveData<Any>()

    val loading: LiveData<Boolean>
        get() = mLoading
    protected val mLoading = MutableLiveData<Boolean>()

    val logoutRedirect: LiveData<Unit> = MutableLiveData()
    val noHasConnection: LiveData<Unit> = MutableLiveData()
    protected fun <T> LiveData<T>.post(data: T) {
        if (this is MutableLiveData<T>) {
            postValue(data)
        }
    }


    fun setLoading(isLoading: Boolean) {
        mLoading.postValue(isLoading)
    }


}