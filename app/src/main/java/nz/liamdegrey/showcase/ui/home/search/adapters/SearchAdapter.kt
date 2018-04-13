package nz.liamdegrey.showcase.ui.home.search.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import nz.liamdegrey.showcase.models.Joke
import nz.liamdegrey.showcase.ui.home.search.views.JokeView
import java.util.*

class SearchAdapter : RecyclerView.Adapter<JokeView.Holder>() {
    private val jokes by lazy { ArrayList<Joke>() }


    fun updateJokes(jokes: List<Joke>) {
        this.jokes.clear()
        this.jokes.addAll(jokes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeView.Holder =
            JokeView.Holder(parent.context)


    override fun onBindViewHolder(holder: JokeView.Holder, position: Int) {
        val joke = jokes[position]

        holder.populateView(position, joke)
    }

    override fun getItemCount(): Int = jokes.size
}