package net.tiap.moviemvvm.data.api

import io.reactivex.Single
import net.tiap.moviemvvm.data.vo.MovieDetail
import net.tiap.moviemvvm.data.vo.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbInterface {

    @GET("movie/{movie_id}")
    fun getDetailMovie(@Path("movie_id") movie_id: Int): Single<MovieDetail>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>
}