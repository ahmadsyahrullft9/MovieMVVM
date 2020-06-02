package net.tiap.moviemvvm.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.tiap.moviemvvm.data.repo.MovieRepository
import net.tiap.moviemvvm.data.repo.NetworkState
import net.tiap.moviemvvm.data.vo.MovieDetail

class SingleMovieViewModel(
    private val movieRepository: MovieRepository,
    movie_id: Int
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetail: LiveData<MovieDetail> by lazy {
        movieRepository.fetchMovieDetail(compositeDisposable, movie_id)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}