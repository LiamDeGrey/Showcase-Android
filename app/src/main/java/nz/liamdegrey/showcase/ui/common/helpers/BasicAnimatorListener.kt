package nz.liamdegrey.showcase.ui.common.helpers

import android.animation.Animator

open class BasicAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {
        //No-op
    }

    override fun onAnimationEnd(animation: Animator?) {
        //No-op
    }

    override fun onAnimationCancel(animation: Animator?) {
        //No-op
    }

    override fun onAnimationStart(animation: Animator?) {
        //No-op
    }
}