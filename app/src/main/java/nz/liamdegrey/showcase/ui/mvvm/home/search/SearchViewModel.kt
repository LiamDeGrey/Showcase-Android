package nz.liamdegrey.showcase.ui.mvvm.home.search

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.liamdegrey.showcase.Application
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvvm.common.BaseViewModel

class SearchViewModel : BaseViewModel() {
    private val jokeBroker by lazy { Application.instance.jokeBroker }

    val jokes = MutableLiveData<List<Joke>>()


    init {
        isLoading.value = false
        jokes.value = null
    }

    fun searchForJokes(term: String) {
        term.takeUnless { it.isBlank() }
                ?.let {
                    isLoading.value = true

                    jokeBroker.searchForJokes(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally { isLoading.value = false }
                            .subscribe { jokesHolder, error ->
                                error?.let {
                                    jokes.value = null
                                } ?: run {
                                    jokes.value = jokesHolder.jokes
                                }
                            }
                } ?: run {
            jokes.value = null
        }
    }
}