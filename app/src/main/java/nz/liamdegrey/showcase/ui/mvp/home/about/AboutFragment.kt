package nz.liamdegrey.showcase.ui.mvp.home.about

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.fragment_about.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.ui.mvp.common.BaseFragment
import nz.liamdegrey.showcase.ui.shared.common.views.Toolbar


class AboutFragment : BaseFragment<AboutPresenter, AboutViewMask>(),
        AboutViewMask {
    override val layoutResId = R.layout.fragment_about


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        about_messageView.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun initToolbar(toolbar: Toolbar) {
        super.initToolbar(toolbar)

        toolbar.setTitle(R.string.about_title)
        toolbar.setHomeAsBack()
    }

    override fun createPresenter(): AboutPresenter = AboutPresenter()

    //region: ViewMask methods

    override fun updateAboutMessage(aboutMessage: CharSequence) {
        about_messageView.text = aboutMessage
    }

    override fun onEmailClicked(emailAddress: String) {
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, emailAddress)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_email_subject))
        }, getString(R.string.about_email_choose_client)))
    }

    //endregion
}
