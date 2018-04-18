package nz.liamdegrey.showcase.ui.mvvm.home.search

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_search.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar
import nz.liamdegrey.showcase.ui.mvvm.common.BaseFragment
import nz.liamdegrey.showcase.ui.mvp.home.search.adapters.SearchAdapter
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment() {
    private val searchAdapter by lazy { SearchAdapter() }

    override val viewModel by lazy { ViewModelProviders.of(this).get(SearchViewModel::class.java) }
    override val layoutResId = R.layout.fragment_search


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        search_jokesList.adapter = searchAdapter

        subscribe(RxTextView.textChanges(search_termInput)
                .debounce(TEXT_INPUT_SEARCH_DELAY, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .map({ input -> input.toString() })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { term ->
                    viewModel.searchForJokes(term)
                })
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.search_title)
        toolbar.setHomeAsBack()
    }

    companion object {
        private const val TEXT_INPUT_SEARCH_DELAY = 500L
    }
}