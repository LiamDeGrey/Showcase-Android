package nz.liamdegrey.showcase.ui.mvp.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.shared.common.views.LoadingView
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar

abstract class BaseActivity<Presenter : BasePresenter<ViewMask>, ViewMask : BaseViewMask> : AppCompatActivity(),
        BaseViewMask, Toolbar.Callbacks {
    protected var presenter: Presenter? = null

    private var topFragmentTag: String? = null
    private var isLoading: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.run {
            topFragmentTag = topFragmentTag ?: getString(SAVED_STATE_TOP_FRAGMENT_TAG, null)
        }

        if (presenter == null) {
            presenter = createPresenter()
            savedInstanceState?.let {
                presenter?.restoreState(it)
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        presenter?.attachViewMask(this as ViewMask)

        findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setCallbacks(this)
            initToolbar(toolbar)
        }
    }

    override fun onStart() {
        super.onStart()

        presenter?.onStart()
    }

    override fun onStop() {
        super.onStop()

        presenter?.onStop()

        currentFocus?.let { focusedView ->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter?.saveState(outState)

        outState.putString(SAVED_STATE_TOP_FRAGMENT_TAG, topFragmentTag)

        super.onSaveInstanceState(outState)
    }

    protected open fun initToolbar(toolbar: Toolbar) {}

    override fun onBackPressed() {
        if (isLoading) {
            return
        }

        val fragment = supportFragmentManager.findFragmentByTag(topFragmentTag) as BaseFragment<*, *>?
        if (fragment == null || !fragment.handleBackPress()) {
            super.onBackPressed()
        }
    }

    protected open fun consumeBackPress() {
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment = supportFragmentManager.findFragmentByTag(topFragmentTag) as BaseFragment<*, *>?
        if (fragment == null || !fragment.consumeActivityResult(requestCode, resultCode, data)) {
            consumeActivityResult(requestCode, resultCode, data)
        }
    }

    protected open fun consumeActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    protected fun startActivity(activityClass: Class<out BaseActivity<*, *>>) {
        val intent = Intent(this, activityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    protected abstract fun createPresenter(): Presenter

    //region: Toolbar methods

    override fun onHomeClicked() {
        finish()
    }

    override fun onExtraClicked() {
        //No-op
    }

    //endregion

    //region: ViewMask methods

    override fun setLoading(loading: Boolean) {
        findViewById<LoadingView>(R.id.loadingView)?.let { loadingView ->
            isLoading = loading
            loadingView.loading = loading
        }
    }

    override fun showFragment(fragment: BaseFragment<*, *>) {
        if (supportFragmentManager.findFragmentByTag(fragment.fragmentTag) == null) {
            topFragmentTag = fragment.fragmentTag
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_animate_in, 0, 0, R.anim.fragment_animate_out)
                    .add(android.R.id.content, fragment, fragment.fragmentTag)
                    .addToBackStack(fragment.fragmentTag)
                    .commit()
        }
    }

    override fun closeFragment() {
        supportFragmentManager.popBackStackImmediate()
    }

    //endregion

    companion object {
        private const val SAVED_STATE_TOP_FRAGMENT_TAG = "topFragmentTag"
    }
}
