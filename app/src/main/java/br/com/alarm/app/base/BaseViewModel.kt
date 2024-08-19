package br.com.alarm.app.base

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alarm.app.domain.repositories.track_repository.TrackRepositoryContract
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var trackRepository: TrackRepositoryContract

    val errorMessage: LiveData<Any>
        get() = mErrorMessage
    protected val mErrorMessage = MutableLiveData<Any>()

    val loading: LiveData<Boolean>
        get() = mLoading
    protected val mLoading = MutableLiveData<Boolean>()

    val noHasConnection: LiveData<Unit> = MutableLiveData()
    protected fun <T> LiveData<T>.post(data: T) {
        if (this is MutableLiveData<T>) {
            postValue(data)
        }
    }

    protected fun defaultLaunch(
        exceptionHandler: ((Throwable) -> Unit)? = null,
        @StringRes defaultErrorMessage: Int? = null,
        loaderLiveData: MutableLiveData<Boolean>? = mLoading,
        dispatcher: CoroutineContext = EmptyCoroutineContext,
        function: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            loaderLiveData?.postValue(true)

            try {
                function.invoke()
            } catch (throwable: Throwable) {
                trackRepository.emitThrow(throwable)
                throwable.printStackTrace()
                defaultCatch(throwable, exceptionHandler, defaultErrorMessage)
            }
            loaderLiveData?.postValue(false)
        }
    }

    private fun defaultCatch(
        throwable: Throwable,
        exceptionHandler: ((Throwable) -> Unit)? = null,
        defaultErrorMessage: Int? = null
    ) {
        when {
            exceptionHandler != null -> exceptionHandler.invoke(throwable)
            defaultErrorMessage != null -> defaultErrorMessage.let { mErrorMessage.postValue(it) }
        }
    }

    fun setLoading(isLoading: Boolean) {
        mLoading.postValue(isLoading)
    }

    fun setScreenTrack(screenName: String) {
        defaultLaunch { trackRepository.setScreenTrack(screenName) }
    }
}