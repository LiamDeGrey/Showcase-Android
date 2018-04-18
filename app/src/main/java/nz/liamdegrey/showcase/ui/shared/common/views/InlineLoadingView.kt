package nz.liamdegrey.showcase.ui.shared.common.views

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.support.graphics.drawable.Animatable2Compat
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import nz.liamdegrey.showcase.R
import kotlin.properties.Delegates


class InlineLoadingView : AppCompatImageView {
    var loading by Delegates.observable(false) { _, oldValue, newValue ->
        if (newValue && !oldValue) {
            visibility = View.VISIBLE
            rotatableDrawable.start()
        }
    }

    private val rotatableDrawable: Animatable
        get() = drawable as Animatable

    private val animationCallback by lazy { createAnimationCallbacks() }


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        visibility = View.GONE
        isClickable = true

        setImageDrawable(AnimatedVectorDrawableCompat.create(context, R.drawable.refresh_animated)?.apply {
            registerAnimationCallback(animationCallback)
        })
    }

    //region: Private methods

    private fun createAnimationCallbacks(): Animatable2Compat.AnimationCallback =
            object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)

                    if (loading) {
                        rotatableDrawable.start()
                    } else {
                        visibility = View.GONE
                    }
                }
            }
}
