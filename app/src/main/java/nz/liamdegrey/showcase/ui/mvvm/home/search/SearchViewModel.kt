package nz.liamdegrey.showcase.ui.mvvm.home.search

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.liamdegrey.showcase.Application
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvvm.common.BaseViewModel

class SearchViewModel : BaseViewModel() {
    private val jokeBroker by lazy { Application.instance.jokeBroker }

    val showNoContentView = MutableLiveData<Boolean>()
    val jokes = MutableLiveData<List<Joke>>()


    //region: Public methods

    fun searchForJokes(term: String) {
        showNoContentView(false)

        term.takeUnless { it.isBlank() }
                ?.let {
                    updateJokes(null)
                    setLoading(true)

                    jokeBroker.searchForJokes(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally { setLoading(false) }
                            .subscribe { jokesHolder, error ->
                                error?.let {
                                    showNoContentView(true)
                                } ?: run {
                                    showNoContentView(jokesHolder.jokes.isEmpty())
                                    updateJokes(jokesHolder.jokes)
                                }
                            }
                } ?: run {
            updateJokes(null)
        }
    }

    //endregion

    //region: View methods

    private fun showNoContentView(show: Boolean) {
        showNoContentView.value = show
    }

    private fun updateJokes(jokes: List<Joke>?) {
        this.jokes.value = jokes
    }

    //endregion
}