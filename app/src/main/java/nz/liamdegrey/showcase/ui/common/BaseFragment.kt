package nz.liamdegrey.showcase.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.extensions.hideKeyboard
import nz.liamdegrey.showcase.ui.common.views.LoadingView
import nz.liamdegrey.showcase.ui.common.views.Toolbar

abstract class BaseFragment<Presenter : BasePresenter<ViewMask>, ViewMask : BaseViewMask> : Fragment(),
        BaseViewMask, Toolbar.Callbacks {
    val fragmentTag = javaClass.name!!

    protected var presenter: Presenter? = null

    private var isLoading: Boolean = false

    @get:LayoutRes
    protected abstract val layoutResId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutResId, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (presenter == null) {
            presenter = createPresenter()
            savedInstanceState?.let {
                presenter?.restoreState(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewCreated(view, savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        presenter?.attachViewMask(this as ViewMask)

        view.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setCallbacks(this)
            initToolbar(toolbar)
        }

        view.isClickable = true
        view.setOnClickListener {
            hideKeyboard(view)
        }
    }

    protected abstract fun viewCreated(view: View, savedInstanceState: Bundle?)

    override fun onStart() {
        super.onStart()

        presenter?.onStart()
    }

    override fun onStop() {
        super.onStop()

        presenter?.onStop()

        activity?.currentFocus?.let { focusedView ->
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)
                    ?.hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter?.detachView()
    }

    protected open fun initToolbar(toolbar: Toolbar) {}

    fun handleBackPress(): Boolean = isLoading || consumeBackPress()

    protected open fun consumeBackPress(): Boolean = false

    open fun consumeActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean = false

    protected abstract fun createPresenter(): Presenter

    //region: Toolbar methods

    override fun onHomeClicked() {
        closeFragment()
    }

    override fun onExtraClicked() {
        //No-op
    }

    //endregion

    //region: ViewMask methods

    override fun setLoading(loading: Boolean) {
        view?.findViewById<LoadingView>(R.id.loadingView)?.let { loadingView ->
            isLoading = loading
            loadingView.loading = loading
        }
    }

    override fun showFragment(fragment: BaseFragment<*, *>) {
        (activity as BaseViewMask).showFragment(fragment)
    }

    override fun closeFragment() {
        fragmentManager?.popBackStackImmediate()
    }

    //endregion
}
