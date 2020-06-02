package net.tiap.moviemvvm.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import net.tiap.moviemvvm.data.repo.MoviePagedListRepository
import net.tiap.moviemvvm.data.repo.MovieRepository
import net.tiap.moviemvvm.data.repo.NetworkState
import net.tiap.moviemvvm.data.vo.Movie

class MainActivityViewModel(private val movieRepository: MoviePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return movieRepository.moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}