package nz.liamdegrey.showcase.ui.home.acknowledgements

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_acknowledgements.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.common.BaseFragment
import nz.liamdegrey.showcase.ui.common.views.Toolbar

class AcknowledgementsFragment : BaseFragment<AcknowledgementsPresenter, AcknowledgementsViewMask>(), AcknowledgementsViewMask {
    override val layoutResId: Int
        get() = R.layout.fragment_acknowledgements


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        acknowledgements_webView.loadUrl("file:///android_asset/licenses.html")
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.acknowledgements_title)
        toolbar.setHomeAsBack()
    }

    override fun createPresenter(): AcknowledgementsPresenter = AcknowledgementsPresenter()
}
