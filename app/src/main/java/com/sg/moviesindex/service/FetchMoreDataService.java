package com.sg.moviesindex.service;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.sg.moviesindex.adapter.MoviesAdapter;
import com.sg.moviesindex.config.BuildConfigs;
import com.sg.moviesindex.model.Discover;
import com.sg.moviesindex.model.DiscoversList;
import com.sg.moviesindex.model.Movie;
import com.sg.moviesindex.model.MoviesList;
import com.sg.moviesindex.service.network.RetrofitInstance;
import com.sg.moviesindex.service.network.TMDbService;
import com.sg.moviesindex.utils.DiscoverToMovie;
import com.sg.moviesindex.view.MainActivity;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FetchMoreDataService {
    private Observable<MoviesList> observableMovie;
    private final RecyclerView recyclerView;
    private ArrayList<Movie> movieList;
    private int totalPages;
    private int totalPagesGenre;
    private ArrayList<Discover> discovers = new ArrayList<>();
    private final CompositeDisposable compositeDisposable;
    private final MoviesAdapter moviesAdapter;
    private final Context context;

    public FetchMoreDataService(RecyclerView recyclerView, ArrayList<Movie> movieList, CompositeDisposable compositeDisposable, Context context, MoviesAdapter moviesAdapter) {
        this.recyclerView = recyclerView;
        this.movieList = movieList;
        this.compositeDisposable = compositeDisposable;
        this.context = context;
        this.moviesAdapter = moviesAdapter;
    }

    public void loadMore(int a, final int pages) {
        final TMDbService TMDbService = RetrofitInstance.getTMDbService(context);
        String ApiKey = BuildConfigs.apiKey;
        if (a == 0) {
            observableMovie = TMDbService.getPopularMoviesWithRx(ApiKey, pages);
        } else if (a == 3) {
            observableMovie = TMDbService.getTopRatedMoviesWithRx(ApiKey, pages);
        } else if (a == 2) {
            observableMovie = TMDbService.getUpcomingMoviesWithRx(ApiKey, pages, MainActivity.region);
        } else if (a == 1) {
            observableMovie = TMDbService.getNowPlayingWithRx(ApiKey, pages, MainActivity.region);
        }
        compositeDisposable.add(observableMovie.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MoviesList>() {
                    @Override
                    public void onNext(MoviesList moviesList) {
                        if (moviesList != null && moviesList.getMovies() != null) {
                            if (pages == 1) {
                                movieList = (ArrayList<Movie>) moviesList.getMovies();
                                totalPages = moviesList.getTotalPages();
                                recyclerView.setAdapter(moviesAdapter);
                            } else {
                                ArrayList<Movie> movies = (ArrayList<Movie>) moviesList.getMovies();
                                for (Movie movie : movies) {
                                    movieList.add(movie);
                                    moviesAdapter.notifyItemInserted(movieList.size() - 1);
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Error! Check your internet connection.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }


    public void loadMoreGenres(final int pages) {
        final TMDbService TMDbService = RetrofitInstance.getTMDbService(context);
        String ApiKey = BuildConfigs.apiKey;
        Observable<DiscoversList> observableDB = TMDbService.discover(ApiKey, Long.toString(MainActivity.genreid), false, false, pages, "popularity.desc", null, null, null, null);
        compositeDisposable.add(
                observableDB.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<DiscoversList>() {
                            @Override
                            public void onNext(DiscoversList discoversList) {
                                if (discoversList != null && discoversList.getResults() != null) {
                                    if (pages == 1) {
                                        discovers = (ArrayList<Discover>) discoversList.getResults();
                                        totalPagesGenre = discoversList.getTotalPages();
                                        recyclerView.setAdapter(moviesAdapter);
                                    } else {
                                        ArrayList<Discover> discovers = (ArrayList<Discover>) discoversList.getResults();
                                        DiscoverToMovie discoverToMovie = new DiscoverToMovie(discovers);
                                        ArrayList<Movie> movies = discoverToMovie.getMovies();
                                        for (Movie movie : movies) {
                                            movieList.add(movie);
                                            moviesAdapter.notifyItemInserted(movieList.size() - 1);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(context, "Error! Check your internet connection.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

}
