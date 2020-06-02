package net.tiap.moviemvvm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import net.tiap.moviemvvm.adapter.PopularMoviePagedAdapter
import net.tiap.moviemvvm.data.api.TheMovieDbClient
import net.tiap.moviemvvm.data.api.TheMovieDbInterface
import net.tiap.moviemvvm.data.repo.MoviePagedListRepository
import net.tiap.moviemvvm.data.repo.NetworkState
import net.tiap.moviemvvm.data.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var movieViewModel: MainActivityViewModel
    lateinit var moviePagedListRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val theMovieDbInterface: TheMovieDbInterface = TheMovieDbClient.getClien()
        moviePagedListRepository = MoviePagedListRepository(theMovieDbInterface)
        movieViewModel = getViewModel()

        val moviePagedAdapter = PopularMoviePagedAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = moviePagedAdapter.getItemViewType(position)
                if (viewType == moviePagedAdapter.MOVIE_ITEM_TYPE) return 1
                else return 3
            }
        }

        rv_list_movie.layoutManager = gridLayoutManager
        rv_list_movie.setHasFixedSize(true)
        rv_list_movie.adapter = moviePagedAdapter

        movieViewModel.moviePagedList.observe(this, Observer {
            moviePagedAdapter.submitList(it)
        })

        movieViewModel.networkState.observe(this, Observer {
            progress_bar.visibility =
                if (movieViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (movieViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!movieViewModel.listIsEmpty()) {
                moviePagedAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(moviePagedListRepository) as T
            }

        })[MainActivityViewModel::class.java]
    }
}
