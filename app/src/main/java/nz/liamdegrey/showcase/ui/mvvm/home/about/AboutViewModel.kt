package nz.liamdegrey.showcase.ui.mvvm.home.about

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.extensions.styleStringPortion
import nz.liamdegrey.showcase.ui.mvvm.common.BaseViewModel

class AboutViewModel : BaseViewModel() {
    val aboutMessage = MutableLiveData<CharSequence>()
    val sendEmailToAddress = MutableLiveData<String>()


    //region: Public methods

    fun setupAboutText(context: Context) {
        val emailAddress = context.resources.getString(R.string.about_body_link)
        val aboutMessage = context.resources.getString(R.string.about_body)
                .styleStringPortion(
                        object : ClickableSpan() {
                            override fun onClick(widget: View?) {
                                sendEmailToAddress(emailAddress)
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
        this.aboutMessage.value = aboutMessage
    }

    private fun sendEmailToAddress(address: String) {
        sendEmailToAddress.value = address//TODO: Not sent every time the user updates
    }

    //endregion
}