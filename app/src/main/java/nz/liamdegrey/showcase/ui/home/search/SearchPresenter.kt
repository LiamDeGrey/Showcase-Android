package nz.liamdegrey.showcase.ui.home.search

import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.liamdegrey.showcase.Application
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.common.BasePresenter
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
                .debounce(500, TimeUnit.MILLISECONDS)
                .map({ input -> input.toString() })

        subscribe(textWatcher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ term ->
                    searchWithTerm(term)
                }))
    }

    //endregion

    //region: Private methods

    private fun searchWithTerm(term: String) {
        term.takeUnless { it.isBlank() }
                ?.let {
                    subscribe(jokeBroker.searchForJokes(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { jokesHolder, error ->
                                error?.let {
                                    updateJokes(ArrayList())
                                } ?: run {
                                    updateJokes(jokesHolder.jokes)
                                }
                            })
                } ?: run {
            updateJokes(ArrayList())
        }
    }

    //endregion

    //region: ViewMask methods

    private fun updateJokes(jokes: List<Joke>) {
        getViewMask()?.updateJokes(jokes)
    }

    //endregion
}
