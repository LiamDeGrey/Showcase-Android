package nz.liamdegrey.showcase.ui.common

interface BaseViewMask {

    fun setLoading(loading: Boolean)

    fun showFragment(fragment: BaseFragment<*, *>)

    fun closeFragment()

}
