package nz.liamdegrey.showcase.ui.mvvm.common

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import io.reactivex.disposables.Disposable
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.extensions.hideKeyboard
import nz.liamdegrey.showcase.ui.shared.common.views.LoadingView
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar
import java.util.*

abstract class BaseFragment : Fragment(), Toolbar.Callbacks {
    val fragmentTag = javaClass.name!!

    protected abstract val viewModel: BaseViewModel

    private var subscriptions: MutableList<Disposable>? = null
    private var isLoading: Boolean = false


    @get:LayoutRes
    protected abstract val layoutResId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutResId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(this, Observer {
            setLoading(it ?: false)
        })

        viewCreated(view, savedInstanceState)

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

    override fun onStop() {
        super.onStop()

        activity?.currentFocus?.let { focusedView ->
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)
                    ?.hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        subscriptions?.forEach { it.dispose() }
        subscriptions = null
    }

    protected open fun initToolbar(toolbar: Toolbar) {}

    fun handleBackPress(): Boolean = isLoading || consumeBackPress()

    protected open fun consumeBackPress(): Boolean = false

    open fun consumeActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean = false

    //region: Toolbar methods

    override fun onHomeClicked() {
        closeFragment()
    }

    override fun onExtraClicked() {
        //No-op
    }

    //endregion

    //region: Protected methods

    protected open fun setLoading(loading: Boolean) {
        view?.findViewById<LoadingView>(R.id.loadingView)?.let { loadingView ->
            isLoading = loading
            loadingView.loading = loading
        }
    }

    protected fun subscribe(subscription: Disposable) {
        if (subscriptions == null) {
            subscriptions = ArrayList()
        }

        subscriptions?.add(subscription)
    }

    protected fun showFragment(fragment: BaseFragment) {
        (activity as BaseActivity).showFragment(fragment)
    }

    protected fun closeFragment() {
        fragmentManager?.popBackStackImmediate()
    }

    //endregion
}