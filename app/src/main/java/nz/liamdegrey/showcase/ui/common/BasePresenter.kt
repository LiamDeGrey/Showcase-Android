package nz.liamdegrey.showcase.ui.common

import android.os.Bundle
import android.support.annotation.CallSuper
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.util.*

abstract class BasePresenter<ViewMask : BaseViewMask> {
    private var subscriptions: MutableList<Disposable>? = null
    private var viewMask: WeakReference<ViewMask>? = null
    protected var viewHidden = false


    fun attachViewMask(view: ViewMask) {
        viewMask = WeakReference(view)

        onViewAttached()
    }

    fun detachView() {
        subscriptions?.forEach { it.dispose() }
        subscriptions = null

        setLoading(false)
        viewMask = null

        onViewDetached()
    }

    protected abstract fun onViewAttached()

    protected abstract fun onViewDetached()

    @CallSuper
    open fun onStart() {
        viewHidden = false
    }

    @CallSuper
    fun onStop() {
        viewHidden = true
    }

    open fun restoreState(state: Bundle) {}

    open fun saveState(state: Bundle) {}

    protected fun getViewMask(): ViewMask? = viewMask?.get()

    protected fun subscribe(subscription: Disposable) {
        if (subscriptions == null) {
            subscriptions = ArrayList()
        }

        subscriptions?.add(subscription)
    }

    //region: ViewMask methods

    protected fun setLoading(loading: Boolean) {
        getViewMask()?.setLoading(loading)
    }

    protected fun showFragment(fragment: BaseFragment<*, *>) {
        getViewMask()?.showFragment(fragment)
    }

    protected fun closeFragment() {
        getViewMask()?.closeFragment()
    }

    //endregion
}
