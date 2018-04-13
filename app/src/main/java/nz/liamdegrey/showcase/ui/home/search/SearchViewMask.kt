package nz.liamdegrey.showcase.ui.home.search

import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.common.BaseViewMask

interface SearchViewMask : BaseViewMask {

    fun updateJokes(jokes: List<Joke>)

}
