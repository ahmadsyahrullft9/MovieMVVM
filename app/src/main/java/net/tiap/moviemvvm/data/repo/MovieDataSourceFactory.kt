package net.tiap.moviemvvm.data.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import net.tiap.moviemvvm.data.api.TheMovieDbInterface
import net.tiap.moviemvvm.data.vo.Movie


class MovieDataSourceFactory(
    private val theMovieDbInterface: TheMovieDbInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(theMovieDbInterface, compositeDisposable)
        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }

}