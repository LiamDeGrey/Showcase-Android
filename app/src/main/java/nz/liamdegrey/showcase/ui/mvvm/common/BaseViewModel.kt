package nz.liamdegrey.showcase.ui.mvvm.common

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel


abstract class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()


    init {
        setLoading(false)
    }

    //region: View methods

    protected fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }

    //endregion
}