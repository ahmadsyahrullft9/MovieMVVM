package net.tiap.moviemvvm.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import net.tiap.moviemvvm.data.Conf
import net.tiap.moviemvvm.data.api.TheMovieDbInterface
import net.tiap.moviemvvm.data.vo.Movie

class MoviePagedListRepository(private val theMovieDbInterface: TheMovieDbInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(theMovieDbInterface, compositeDisposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Conf.POST_PER_PAGE)
            .build()
        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.movieLiveDataSource,
            MovieDataSource::networkState
        )
    }

}