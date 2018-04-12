package nz.liamdegrey.showcase.ui.home

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.liamdegrey.showcase.Application
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.common.BasePresenter

class HomePresenter : BasePresenter<HomeViewMask>() {
    private val jokesBroker by lazy { Application.instance.jokeBroker }


    override fun onViewAttached() {
        setLoading(true)

        subscribe(jokesBroker.getRandomJokes((Math.random() * 19 + 1).toInt())//between 1 and 20
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { setLoading(false) }
                .subscribe { jokesHolder, error ->
                    error?.let {
                        showNoContentView(true)
                    } ?: run {
                        showNoContentView(false)
                        populateJokes(jokesHolder.jokes)
                    }
                })
    }

    override fun onViewDetached() {
    }

    //region: Presenter methods

    fun onExtraClicked() {

    }

    fun onAcknowledgementsClicked() {

    }

    fun onLikedTheSplashClicked() {
        goToSplashActivity()
    }

    //endregion

    //region: Private methods

    //endregion

    //region: ViewMask methods

    private fun showNoContentView(show: Boolean) {
        getViewMask()?.showNoContentView(show)
    }

    private fun populateJokes(jokes: List<Joke>) {
        getViewMask()?.populateJokes(jokes)
    }

    private fun goToSplashActivity() {
        getViewMask()?.goToSplashActivity()
    }

    //endregion
}