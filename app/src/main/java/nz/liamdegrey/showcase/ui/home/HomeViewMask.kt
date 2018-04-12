package nz.liamdegrey.showcase.ui.home

import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.common.BaseViewMask

interface HomeViewMask : BaseViewMask {

    fun showNoContentView(show: Boolean)

    fun populateJokes(jokes: List<Joke>)

    fun goToSplashActivity()

}
