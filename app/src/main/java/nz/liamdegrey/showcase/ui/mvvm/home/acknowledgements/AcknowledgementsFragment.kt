package nz.liamdegrey.showcase.ui.mvvm.home.acknowledgements

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_acknowledgements.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.mvvm.common.BaseFragment
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar

class AcknowledgementsFragment : BaseFragment() {
    override val viewModel by lazy { ViewModelProviders.of(this).get(AcknowledgementsViewModel::class.java) }
    override val layoutResId = R.layout.fragment_acknowledgements


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        acknowledgements_webView.loadUrl("file:///android_asset/licenses.html")
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.acknowledgements_title)
        toolbar.setHomeAsBack()
    }
}