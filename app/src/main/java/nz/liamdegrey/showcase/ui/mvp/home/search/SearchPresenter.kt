package nz.liamdegrey.showcase.ui.mvp.home.search

import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.liamdegrey.showcase.Application
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.mvp.common.BasePresenter
import java.util.concurrent.TimeUnit


class SearchPresenter : BasePresenter<SearchViewMask>() {
    private val jokeBroker = Application.instance.jokeBroker


    override fun onViewAttached() {
    }

    override fun onViewDetached() {
    }

    //region: Presenter methods

    fun setupTextWatcher(textInput: EditText) {
        val textWatcher = RxTextView.textChanges(textInput)
                .debounce(TEXT_INPUT_SEARCH_DELAY, TimeUnit.MILLISECONDS)
                .map({ input -> input.toString() })
                .distinctUntilChanged()

        subscribe(textWatcher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ term ->
                    searchForJokes(term)
                }))
    }

    //endregion

    //region: Private methods

    private fun searchForJokes(term: String) {
        showNoContentView(false)

        term.takeUnless { it.isBlank() }
                ?.let {
                    updateJokes(null)
                    setLoading(true)

                    jokeBroker.searchForJokes(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doFinally { setLoading(false) }
                            .subscribe { jokesHolder, error ->
                                error?.let {
                                    showNoContentView(true)
                                } ?: run {
                                    showNoContentView(jokesHolder.jokes.isEmpty())
                                    updateJokes(jokesHolder.jokes)
                                }
                            }
                } ?: run {
            updateJokes(null)
        }
    }

    //endregion

    //region: ViewMask methods

    private fun showNoContentView(show: Boolean) {
        getViewMask()?.showNoContentView(show)
    }

    private fun updateJokes(jokes: List<Joke>?) {
        getViewMask()?.updateJokes(jokes.orEmpty())
    }

    //endregion

    companion object {
        private const val TEXT_INPUT_SEARCH_DELAY = 500L
    }
}
