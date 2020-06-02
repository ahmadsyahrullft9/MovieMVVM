package net.tiap.moviemvvm.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.tiap.moviemvvm.data.api.TheMovieDbInterface
import net.tiap.moviemvvm.data.vo.MovieDetail
import java.lang.Exception

class MovieDetailDataSource(
    private val theMovieDbInterface: TheMovieDbInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadMovieDetailResponse = MutableLiveData<MovieDetail>()
    val downloadMovieDetailResponse: LiveData<MovieDetail>
        get() = _downloadMovieDetailResponse

    fun fetchMovieDetail(movie_id: Int) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                theMovieDbInterface.getDetailMovie(movie_id)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadMovieDetailResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailDataSource", it.message.toString())
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("MovieDetailDataSource", e.message.toString())
        }
    }
}