package nz.liamdegrey.showcase.ui.home.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_joke.view.*
import nz.liamdegrey.showcase.R

class JokeView private constructor(context: Context) : FrameLayout(context) {

    init {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        inflate(context, R.layout.view_joke, this)
    }

    class JokeViewHolder(context: Context) : RecyclerView.ViewHolder(JokeView(context)) {

        fun populateView(title: String, number: Int) {
            view.joke_titleView.text = title
//            view.joke_numberView.setImageDrawable(CharacterDrawable(view.context, number.toChar()))
        }

        private val view: JokeView
            get() = itemView as JokeView
    }
}
