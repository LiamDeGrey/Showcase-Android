package nz.liamdegrey.showcase.ui.shared.common.views

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_toolbar.view.*
import nz.liamdegrey.showcase.R

class Toolbar : RelativeLayout, View.OnClickListener {
    interface Callbacks {
        fun onHomeClicked()
        fun onExtraClicked()
    }

    private lateinit var callbacks: Callbacks

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        inflate(context, R.layout.view_toolbar, this)

        ViewCompat.setElevation(this, resources.getDimension(R.dimen.elevation))

        toolbar_button_home.setOnClickListener(this)
        toolbar_button_extra.setOnClickListener(this)
    }

    fun setCallbacks(callbacks: Callbacks) {
        this.callbacks = callbacks
    }

    fun setTitle(@StringRes titleRes: Int) {
        toolbar_title.setText(titleRes)
    }

    fun setTitle(title: String) {
        toolbar_title.text = title
    }

    fun setHomeAsBack() {
        toolbar_button_home_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_chevron_left))
        toolbar_button_home_text.setText(R.string.back)
        toolbar_button_home.visibility = View.VISIBLE
    }

    fun setHomeAsDrawer() {
        toolbar_button_home_image.setImageResource(R.drawable.ic_menu)
        toolbar_button_home.visibility = View.VISIBLE
    }

    fun setExtraAsSearch() {
        toolbar_button_extra.setImageResource(R.drawable.ic_search)
        toolbar_button_extra.visibility = View.VISIBLE
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.toolbar_button_home -> callbacks.onHomeClicked()
            R.id.toolbar_button_extra -> callbacks.onExtraClicked()
        }
    }
}
