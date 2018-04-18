package nz.liamdegrey.showcase.ui.mvp.home

import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvp.common.BaseViewMask

interface HomeViewMask : BaseViewMask {

    fun showWelcomeMessage()

    fun updateJokes(jokes: List<Joke>)

    fun goToSplashActivity()

}
