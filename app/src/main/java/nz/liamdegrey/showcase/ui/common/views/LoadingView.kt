package nz.liamdegrey.showcase.ui.common.views

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import nz.liamdegrey.showcase.R


class LoadingView : FrameLayout, Animator.AnimatorListener {
    private val loaderAnimation: ObjectAnimator

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.semiTransparent))
        id = R.id.loadingView

        val loaderImage = AppCompatImageView(context)
        loaderImage.setImageResource(R.drawable.ic_refresh_large)
        loaderImage.scaleType = ImageView.ScaleType.CENTER_INSIDE

        loaderAnimation = ObjectAnimator.ofFloat(loaderImage, View.ROTATION, 0f, 360f)
        loaderAnimation.repeatMode = ValueAnimator.RESTART
        loaderAnimation.interpolator = AccelerateDecelerateInterpolator()
        loaderAnimation.duration = 500
        loaderAnimation.addListener(this)

        addView(loaderImage)
        visibility = View.GONE
        isClickable = true
    }

    fun setLoading(loading: Boolean) {
        if (loading) {
            loaderAnimation.repeatCount = ValueAnimator.INFINITE
            loaderAnimation.start()
        } else {
            loaderAnimation.repeatCount = 0
        }
    }

    override fun onAnimationEnd(p0: Animator?) {
        visibility = View.GONE
    }

    override fun onAnimationStart(p0: Animator?) {
        visibility = View.VISIBLE
    }

    override fun onAnimationRepeat(p0: Animator?) {
    }

    override fun onAnimationCancel(p0: Animator?) {
    }
}
