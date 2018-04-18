package nz.liamdegrey.showcase.ui.mvvm.home.about

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.fragment_about.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.mvvm.common.BaseFragment
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar

class AboutFragment : BaseFragment() {
    override val viewModel by lazy { ViewModelProviders.of(this).get(AboutViewModel::class.java) }
    override val layoutResId = R.layout.fragment_about


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        about_messageView.movementMethod = LinkMovementMethod.getInstance()

        viewModel.aboutMessage.observe(this, Observer {
            updateAboutMessage(it)
        })
        viewModel.sendEmailToAddress.observe(this, Observer {
            sendEmail(it)
        })

        viewModel.setupAboutText(view.context)
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.about_title)
        toolbar.setHomeAsBack()
    }

    //region: private methods

    private fun updateAboutMessage(aboutMessage: CharSequence?) {
        about_messageView.text = aboutMessage
    }

    private fun sendEmail(address: String?) {
        address?.let {
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_EMAIL, it)
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_email_subject))
            }, getString(R.string.about_email_choose_client)))
        }
    }

    //endregion
}