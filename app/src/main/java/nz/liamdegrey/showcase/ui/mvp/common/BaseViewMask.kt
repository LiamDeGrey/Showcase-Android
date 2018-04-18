package nz.liamdegrey.showcase.ui.mvp.common

interface BaseViewMask {

    fun setLoading(loading: Boolean)

    fun showFragment(fragment: BaseFragment<*, *>)

    fun closeFragment()

}
