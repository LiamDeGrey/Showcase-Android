package nz.liamdegrey.showcase.ui.home.about

import android.os.Bundle
import android.view.View
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.common.BaseFragment
import nz.liamdegrey.showcase.ui.common.views.Toolbar


class AboutFragment : BaseFragment<AboutPresenter, AboutViewMask>(),
        AboutViewMask {
    override val layoutResId: Int
        get() = R.layout.fragment_about


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.about_title)
        toolbar.setHomeAsBack()
    }

    override fun createPresenter(): AboutPresenter = AboutPresenter()
}
