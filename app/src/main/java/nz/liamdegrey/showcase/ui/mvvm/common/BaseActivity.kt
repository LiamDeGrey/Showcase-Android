package nz.liamdegrey.showcase.ui.mvvm.common

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import io.reactivex.disposables.Disposable
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.shared.common.views.LoadingView
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar
import java.util.*

abstract class BaseActivity : AppCompatActivity(), Toolbar.Callbacks {
    protected abstract val viewModel: BaseViewModel

    private var subscriptions: MutableList<Disposable>? = null
    private var topFragmentTag: String? = null
    private var isLoading: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.run {
            topFragmentTag = topFragmentTag ?: getString(SAVED_STATE_TOP_FRAGMENT_TAG, null)
        }

        viewModel.isLoading.observe(this, Observer {
            setLoading(it ?: false)
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setCallbacks(this)
            initToolbar(toolbar)
        }
    }

    override fun onStop() {
        super.onStop()

        currentFocus?.let { focusedView ->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        subscriptions?.forEach { it.dispose() }
        subscriptions = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SAVED_STATE_TOP_FRAGMENT_TAG, topFragmentTag)

        super.onSaveInstanceState(outState)
    }

    protected open fun initToolbar(toolbar: Toolbar) {}

    override fun onBackPressed() {
        if (isLoading) {
            return
        }

        val fragment = supportFragmentManager.findFragmentByTag(topFragmentTag) as BaseFragment?
        if (fragment == null || !fragment.handleBackPress()) {
            super.onBackPressed()
        }
    }

    protected open fun consumeBackPress() {
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment = supportFragmentManager.findFragmentByTag(topFragmentTag) as BaseFragment?
        if (fragment == null || !fragment.consumeActivityResult(requestCode, resultCode, data)) {
            consumeActivityResult(requestCode, resultCode, data)
        }
    }

    protected open fun consumeActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    protected fun startActivity(activityClass: Class<out BaseActivity>) {
        val intent = Intent(this, activityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    //region: Toolbar methods

    override fun onHomeClicked() {
        finish()
    }

    override fun onExtraClicked() {
        //No-op
    }

    //endregion

    //region: Public methods

    fun showFragment(fragment: BaseFragment) {
        if (supportFragmentManager.findFragmentByTag(fragment.fragmentTag) == null) {
            topFragmentTag = fragment.fragmentTag
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_animate_in, 0, 0, R.anim.fragment_animate_out)
                    .add(android.R.id.content, fragment, fragment.fragmentTag)
                    .addToBackStack(fragment.fragmentTag)
                    .commit()
        }
    }

    //endregion

    //region: Protected methods

    protected fun subscribe(subscription: Disposable) {
        if (subscriptions == null) {
            subscriptions = ArrayList()
        }

        subscriptions?.add(subscription)
    }

    protected fun closeFragment() {
        supportFragmentManager.popBackStackImmediate()
    }

    //endregion

    //region: Private methods

    private fun setLoading(loading: Boolean) {
        findViewById<LoadingView>(R.id.loadingView)?.let { loadingView ->
            isLoading = loading
            loadingView.loading = loading
        }
    }

    //endregion

    companion object {
        private const val SAVED_STATE_TOP_FRAGMENT_TAG = "topFragmentTag"
    }
}