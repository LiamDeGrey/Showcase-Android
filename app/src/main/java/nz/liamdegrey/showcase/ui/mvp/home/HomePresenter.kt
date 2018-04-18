package nz.liamdegrey.showcase.ui.mvp.home

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.liamdegrey.showcase.Application
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvp.common.BasePresenter
import nz.liamdegrey.showcase.ui.mvp.home.about.AboutFragment
import nz.liamdegrey.showcase.ui.mvp.home.acknowledgements.AcknowledgementsFragment
import nz.liamdegrey.showcase.ui.mvp.home.search.SearchFragment

class HomePresenter : BasePresenter<HomeViewMask>() {
    private val jokesBroker by lazy { Application.instance.jokeBroker }
    private val preferences by lazy { Application.instance.preferences }


    override fun onViewAttached() {
        setLoading(true)

        subscribe(jokesBroker.getRandomJokes((Math.random() * 6 + 4).toInt())//between 4 and 10
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { setLoading(false) }
                .subscribe { jokesHolder, error ->
                    error?.let {
                        showNoContentView(true)
                    } ?: run {
                        showNoContentView(false)
                        populateJokes(jokesHolder.jokes.shuffled())
                    }
                })

        if (!preferences.hasViewedHomeActivity) {
            showWelcomeMessage()
            preferences.hasViewedHomeActivity = true
        }
    }

    override fun onViewDetached() {
    }

    //region: Presenter methods

    fun onExtraClicked() {
        showFragment(SearchFragment())
    }

    fun onAboutClicked() {
        showFragment(AboutFragment())
    }

    fun onAcknowledgementsClicked() {
        showFragment(AcknowledgementsFragment())
    }

    fun onLikedTheSplashClicked() {
        goToSplashActivity()
    }

    //endregion

    //region: Private methods

    //endregion

    //region: ViewMask methods

    private fun showWelcomeMessage() {
        getViewMask()?.showWelcomeMessage()
    }

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