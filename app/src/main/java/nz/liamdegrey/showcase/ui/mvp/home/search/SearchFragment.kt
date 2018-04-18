package nz.liamdegrey.showcase.ui.mvp.home.search

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.fragment_search.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvp.common.BaseFragment
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar
import nz.liamdegrey.showcase.ui.shared.home.search.adapters.SearchAdapter


class SearchFragment : BaseFragment<SearchPresenter, SearchViewMask>(),
        SearchViewMask {
    private val searchAdapter by lazy { SearchAdapter() }

    override val layoutResId = R.layout.fragment_search


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        search_jokesList.adapter = searchAdapter

        presenter?.setupTextWatcher(search_termInput)
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.search_title)
        toolbar.setHomeAsBack()
    }

    override fun createPresenter(): SearchPresenter = SearchPresenter()

    override fun setLoading(loading: Boolean) {

    }

    //region: ViewMask methods

    override fun updateJokes(jokes: List<Joke>) {
        searchAdapter.updateJokes(jokes)

        search_noContent.visibility = if (search_termInput.text.isBlank() || !jokes.isEmpty()) GONE else VISIBLE
    }

    //endregion
}
