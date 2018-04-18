package nz.liamdegrey.showcase.ui.mvp.home.about

import nz.liamdegrey.showcase.ui.mvp.common.BaseViewMask


interface AboutViewMask : BaseViewMask {

    fun updateAboutMessage(aboutMessage: CharSequence)

    fun onEmailClicked(emailAddress: String)

}
