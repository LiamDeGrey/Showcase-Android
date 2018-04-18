package nz.liamdegrey.showcase.ui.mvp.home.about

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import nz.liamdegrey.showcase.Application
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.extensions.styleStringPortion
import nz.liamdegrey.showcase.ui.mvp.common.BasePresenter

class AboutPresenter : BasePresenter<AboutViewMask>() {

    override fun onViewAttached() {
        setupAboutText(Application.instance)
    }

    override fun onViewDetached() {}

    //region: Private methods

    private fun setupAboutText(context: Context) {
        val emailAddress = context.resources.getString(R.string.about_body_link)
        val aboutMessage = context.resources.getString(R.string.about_body)
                .styleStringPortion(
                        object : ClickableSpan() {
                            override fun onClick(widget: View?) {
                                onEmailClicked(emailAddress)
                            }
                        },
                        emailAddress)

        aboutMessage.styleStringPortion(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.blue)),
                emailAddress)

        updateAboutMessage(aboutMessage)
    }

    //endregion

    //region: View methods

    private fun updateAboutMessage(aboutMessage: CharSequence) {
        getViewMask()?.updateAboutMessage(aboutMessage)
    }

    private fun onEmailClicked(emailAddress: String) {
        getViewMask()?.onEmailClicked(emailAddress)
    }


    //endregion
}
