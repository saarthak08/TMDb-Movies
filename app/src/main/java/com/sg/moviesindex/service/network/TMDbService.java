package com.sg.moviesindex.service.network;

import com.sg.moviesindex.model.CastsList;
import com.sg.moviesindex.model.DiscoversList;
import com.sg.moviesindex.model.GenresList;
import com.sg.moviesindex.model.Movie;
import com.sg.moviesindex.model.MoviesList;
import com.sg.moviesindex.model.ReviewsList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbService {

    //@GET("movie/popular")
    //Call<MovieDBResponse> getPopularMovies(@Query("api_key")String apiKey, @Query("page")int pageIndex);

    @GET("movie/popular")
    Observable<MoviesList> getPopularMoviesWithRx(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("movie/top_rated")
    Observable<MoviesList> getTopRatedMoviesWithRx(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("movie/upcoming")
    Observable<MoviesList> getUpcomingMoviesWithRx(@Query("api_key") String apiKey, @Query("page") int pageIndex, @Query("region") String region);

    @GET("movie/now_playing")
    Observable<MoviesList> getNowPlayingWithRx(@Query("api_key") String apiKey, @Query("page") int pageIndex, @Query("region") String region);

    @GET("genre/movie/list")
    Observable<GenresList> getGenresList(@Query("api_key") String apiKey);

    @GET("movie/{movieId}/reviews")
    Observable<ReviewsList> getReviews(@Path("movieId") Long movieId, @Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("movie/{movieId}/credits")
    Observable<CastsList> getCasts(@Path("movieId") Long movieId, @Query("api_key") String apiKey);

    @GET("discover/movie")
    Observable<DiscoversList> discover(@Query("api_key") String apiKey, @Query("with_genres") String genres, @Query("include_adult") Boolean adult,
                                       @Query("include_video") Boolean video, @Query("page") int pageIndex, @Query("sort_by") String sortBy,
                                       @Query("region") String region, @Query("with_original_language") String language,
                                       @Query("release_date.gte") String releaseDateGTE, @Query("release_date.lte") String releaseDateLTE);

    @GET("search/movie")
    Observable<DiscoversList> search(@Query("api_key") String apiKey, @Query("include_adult") Boolean adult, @Query("query") String query, @Query("page") int page);

    @GET("movie/{id}")
    Observable<Movie> getFullMovieInformation(@Path("id") Long id, @Query("api_key") String apiKey);


}
