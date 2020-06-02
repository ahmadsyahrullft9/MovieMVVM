package net.tiap.moviemvvm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detailmovie.*
import net.tiap.moviemvvm.data.Conf
import net.tiap.moviemvvm.data.api.TheMovieDbClient
import net.tiap.moviemvvm.data.api.TheMovieDbInterface
import net.tiap.moviemvvm.data.repo.MovieRepository
import net.tiap.moviemvvm.data.repo.NetworkState
import net.tiap.moviemvvm.data.viewmodel.SingleMovieViewModel
import net.tiap.moviemvvm.data.vo.MovieDetail

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailmovie)

        val movie_id = intent.getIntExtra("id", 1)
        val apiservice: TheMovieDbInterface = TheMovieDbClient.getClien()

        movieRepository = MovieRepository(apiservice)
        viewModel = getViewModel(movie_id)

        viewModel.movieDetail.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(it: MovieDetail) {
        Glide.with(this)
            .asBitmap()
            .load(Conf.POSTER_BASE_URL + it.posterPath)
            .into(iv_movie_poster)
        txt_movie_title.text = it.title
        txt_movie_tagline.text = it.tagline
        txt_movie_status.text = it.status
        txt_movie_rating.text = it.voteAverage.toString()
        txt_movie_subtitle.text = it.overview

        val homePage = it.homepage

        btn_visit_movie.setOnClickListener({
            val intentMovie = Intent(Intent.ACTION_VIEW, Uri.parse(homePage))
            startActivity(intentMovie)
        })
    }

    fun getViewModel(movie_id: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(movieRepository, movie_id) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}