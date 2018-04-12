package nz.liamdegrey.showcase.ui.splash

import android.text.SpannableString
import nz.liamdegrey.showcase.ui.common.BasePresenter

class SplashPresenter(appName: CharSequence) : BasePresenter<SplashViewMask>() {
    private val appName = SpannableString(appName)


    override fun onViewAttached() {
    }

    override fun onViewDetached() {
    }

    //region: ViewMask methods

    private fun updateAnimationText(animationText: SpannableString) {
        getViewMask()?.updateAnimationText(animationText)
    }

    private fun startHomeActivity() {
        getViewMask()?.startHomeActivity()
    }

    //endregion

    companion object {
        private const val SPLASH_SCREEN_TIMEOUT: Long = 2000
        private const val SPLASH_SCREEN_ANIMATION_FRAME: Long = 100
    }
}
