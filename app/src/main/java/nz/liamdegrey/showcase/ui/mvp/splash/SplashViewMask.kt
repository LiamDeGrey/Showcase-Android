package nz.liamdegrey.showcase.ui.mvp.splash

import android.text.SpannableString
import nz.liamdegrey.showcase.ui.mvp.common.BaseViewMask

interface SplashViewMask : BaseViewMask {

    fun updateAnimationText(animationText: SpannableString)

    fun startHomeActivity()
}
