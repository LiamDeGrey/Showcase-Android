package nz.liamdegrey.showcase.ui.mvvm.home.acknowledgements

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.mvvm.common.BaseFragment

class AcknowledgementsFragment : BaseFragment() {
    override val viewModel by lazy { ViewModelProviders.of(this).get(AcknowledgementsViewModel::class.java) }
    override val layoutResId = R.layout.fragment_acknowledgements


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {

    }
}