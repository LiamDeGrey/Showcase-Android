package nz.liamdegrey.showcase.ui.home.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_drawer.view.*
import nz.liamdegrey.showcase.R

class DrawerView : RelativeLayout, View.OnClickListener {
    interface Callbacks {

        fun onAboutClicked()

        fun onSensorClicked()

        fun onAcknowledgementsClicked()

        fun onLikedTheSplashClicked()

        fun closeDrawer()
    }

    lateinit var callbacks: Callbacks

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.view_drawer, this)

        setBackgroundColor(ContextCompat.getColor(context, R.color.darkGrey))

        drawer_aboutBtn.setOnClickListener(this)
        drawer_sensorBtn.setOnClickListener(this)
        drawer_acknowledgementsBtn.setOnClickListener(this)
        drawer_likedTheSplashBtn.setOnClickListener(this)

        isClickable = true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.drawer_aboutBtn -> callbacks.onAboutClicked()
            R.id.drawer_sensorBtn -> callbacks.onSensorClicked()
            R.id.drawer_acknowledgementsBtn -> callbacks.onAcknowledgementsClicked()
            R.id.drawer_likedTheSplashBtn -> callbacks.onLikedTheSplashClicked()
        }

        callbacks.closeDrawer()
    }
}
