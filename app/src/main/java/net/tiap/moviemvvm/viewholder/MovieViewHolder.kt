package net.tiap.moviemvvm.viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie_list.view.*
import net.tiap.moviemvvm.DetailMovieActivity
import net.tiap.moviemvvm.data.Conf
import net.tiap.moviemvvm.data.vo.Movie

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    public fun onBind(movie: Movie?, context: Context) {
        itemView.txt_movie_title.text = movie?.title
        itemView.txt_movie_subtitle.text = movie?.overview

        Glide.with(itemView)
            .asBitmap()
            .load(Conf.POSTER_BASE_URL + movie?.posterPath)
            .into(itemView.iv_movie_poster)

        itemView.setOnClickListener {
            val detailIntent = Intent(context, DetailMovieActivity::class.java)
            detailIntent.putExtra("id", movie?.id)
            context.startActivity(detailIntent)
        }
    }
}