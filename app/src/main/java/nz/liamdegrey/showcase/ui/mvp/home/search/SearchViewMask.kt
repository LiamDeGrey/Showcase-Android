package nz.liamdegrey.showcase.ui.mvp.home.search

import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvp.common.BaseViewMask

interface SearchViewMask : BaseViewMask {

    fun showNoContentView(show: Boolean)

    fun updateJokes(jokes: List<Joke>)

}
