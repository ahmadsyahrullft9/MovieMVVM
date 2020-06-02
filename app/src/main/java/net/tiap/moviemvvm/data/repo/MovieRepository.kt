package net.tiap.moviemvvm.data.repo

import androidx.lifecycle.LiveData
import io.reactivex.disposables.CompositeDisposable
import net.tiap.moviemvvm.data.api.TheMovieDbInterface
import net.tiap.moviemvvm.data.vo.MovieDetail

class MovieRepository(private val theMovieDbInterface: TheMovieDbInterface) {

    lateinit var movieDetailDataSource: MovieDetailDataSource

    fun fetchMovieDetail(
        compositeDisposable: CompositeDisposable,
        movie_id: Int
    ): LiveData<MovieDetail> {
        movieDetailDataSource = MovieDetailDataSource(theMovieDbInterface, compositeDisposable)
        movieDetailDataSource.fetchMovieDetail(movie_id)
        return movieDetailDataSource.downloadMovieDetailResponse
    }

    fun getMovieDetailNetworkState(): LiveData<NetworkState> {
        return movieDetailDataSource.networkState
    }

}