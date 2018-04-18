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
        jokes.value = null
    }

    fun searchForJokes(term: String) {
        term.takeUnless { it.isBlank() }
                ?.let {
                    setLoading(true)

                    jokeBroker.searchForJokes(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally { setLoading(false) }
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