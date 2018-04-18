package nz.liamdegrey.showcase.ui.mvvm.home.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_search.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvvm.common.BaseFragment
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar
import nz.liamdegrey.showcase.ui.shared.home.search.adapters.SearchAdapter
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment() {
    override val viewModel by lazy { ViewModelProviders.of(this).get(SearchViewModel::class.java) }
    override val layoutResId = R.layout.fragment_search

    private val searchAdapter by lazy { SearchAdapter() }


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        search_jokesList.adapter = searchAdapter

        subscribe(RxTextView.textChanges(search_termInput)
                .debounce(TEXT_INPUT_SEARCH_DELAY, TimeUnit.MILLISECONDS)
                .map({ input -> input.toString() })
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { term ->
                    viewModel.searchForJokes(term)
                })

        viewModel.showNoContentView.observe(this, Observer {
            showNoContentView(it ?: false)
        })

        viewModel.jokes.observe(this, Observer {
            updateJokes(it.orEmpty())
        })
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.search_title)
        toolbar.setHomeAsBack()
    }

    override fun setLoading(loading: Boolean) {
        search_loadingView.loading = loading
    }

    //region: Private methods

    private fun showNoContentView(show: Boolean) {
        search_noContent.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun updateJokes(jokes: List<Joke>) {
        searchAdapter.updateJokes(jokes)
    }

    //endregion

    companion object {
        private const val TEXT_INPUT_SEARCH_DELAY = 500L
    }
}