package nz.liamdegrey.showcase.ui.mvp.home.search.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_joke.view.*
import nz.liamdegrey.showcase.R
import nz.liamdegrey.showcase.models.Joke

class JokeView private constructor(context: Context) : LinearLayout(context) {

    init {
        orientation = HORIZONTAL
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        inflate(context, R.layout.view_joke, this)
    }

    class Holder(context: Context) : RecyclerView.ViewHolder(JokeView(context)) {

        fun populateView(number: Int, joke: Joke) {
            view.joke_numberView.updateCharacter(number)
            view.joke_titleView.text = joke.body
        }

        private val view: JokeView
            get() = itemView as JokeView
    }
}
