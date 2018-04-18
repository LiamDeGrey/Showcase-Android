package nz.liamdegrey.showcase.ui.mvvm.home

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.liamdegrey.showcase.Application
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvvm.common.BaseViewModel

class HomeViewModel : BaseViewModel() {
    private val jokesBroker by lazy { Application.instance.jokeBroker }
    private val preferences by lazy { Application.instance.preferences }

    val showWelcomeMessage = MutableLiveData<Boolean>()
    val jokes = MutableLiveData<List<Joke>>()


    init {
        searchForJokes()

        if (!preferences.hasViewedHomeActivity) {
            showWelcomeMessage()
            preferences.hasViewedHomeActivity = true
        }
    }

    //region: Public methods

    fun searchForJokes() {
        setLoading(true)

        jokesBroker.getRandomJokes((Math.random() * 6 + 4).toInt())//between 4 and 10
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { setLoading(false) }
                .subscribe { jokesHolder, error ->
                    error?.let {
                        updateJokes(null)
                    } ?: run {
                        updateJokes(jokesHolder.jokes.shuffled())
                    }
                }
    }

    //endregion

    //region: View methods

    private fun showWelcomeMessage() {
        showWelcomeMessage.value = true
    }

    private fun updateJokes(jokes: List<Joke>?) {
        this.jokes.value = jokes
    }

    //endregion
}