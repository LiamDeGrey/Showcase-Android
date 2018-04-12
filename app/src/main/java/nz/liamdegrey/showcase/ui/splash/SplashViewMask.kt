package nz.liamdegrey.showcase.ui.splash

import android.text.SpannableString
import nz.liamdegrey.showcase.ui.common.BaseViewMask

interface SplashViewMask : BaseViewMask {

    fun updateAnimationText(animationText: SpannableString)

    fun startHomeActivity()
}
