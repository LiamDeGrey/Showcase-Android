package nz.liamdegrey.showcase.ui.mvvm.home.about

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.mvvm.common.BaseFragment

class AboutFragment : BaseFragment() {
    override val viewModel by lazy { ViewModelProviders.of(this).get(AboutViewModel::class.java) }
    override val layoutResId = R.layout.fragment_about


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
    }
}