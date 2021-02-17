package com.sg.moviesindex.service;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sg.moviesindex.config.BuildConfigs;
import com.sg.moviesindex.model.DiscoversList;
import com.sg.moviesindex.model.Genre;
import com.sg.moviesindex.model.GenresList;
import com.sg.moviesindex.model.MoviesList;
import com.sg.moviesindex.service.network.RetrofitInstance;
import com.sg.moviesindex.service.network.TMDbService;
import com.sg.moviesindex.view.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FetchGenresListService implements Serializable {
    private Observable<MoviesList> observableMovie;
    private Observable<DiscoversList> observableDB;
    private final Context context;
    private final ProgressBar progressBar;
    private final LinearLayout linearLayoutError;
    private final Button refreshButtonError;
    private final CompositeDisposable compositeDisposable;
    private final FetchFirstTimeDataService fetchFirstTimeDataService;

    public FetchGenresListService(LinearLayout linearLayout, Button button, Context context, CompositeDisposable compositeDisposable, FetchFirstTimeDataService fetchFirstTimeDataService, ProgressBar progressBar) {
        this.context = context;
        this.linearLayoutError = linearLayout;
        this.refreshButtonError = button;
        this.compositeDisposable = compositeDisposable;
        this.fetchFirstTimeDataService = fetchFirstTimeDataService;
        this.progressBar = progressBar;
    }

    public void getGenresList() {
        final TMDbService TMDbService = RetrofitInstance.getTMDbService(context);
        String ApiKey = BuildConfigs.apiKey;
        MainActivity.drawer = 4;
        Observable<GenresList> genresListObservable = TMDbService.getGenresList(ApiKey);
        try {
            compositeDisposable.add(
                    genresListObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<GenresList>() {
                                @Override
                                public void onNext(@NotNull GenresList genresList) {
                                    if (genresList.getGenres() != null) {
                                        MainActivity.genres = (ArrayList<Genre>) genresList.getGenres();
                                        String[] a = new String[MainActivity.genres.size()];
                                        for (int i = 0; i < MainActivity.genres.size(); i++) {
                                            a[i] = MainActivity.genres.get(i).getName();
                                        }
                                        Dialog dialog = new MaterialAlertDialogBuilder(context).setTitle("Choose a Category").setSingleChoiceItems(a, -1, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                MainActivity.selected = which;
                                                MainActivity.genreid = MainActivity.genres.get(which).getId();
                                                fetchFirstTimeDataService.getFirstGenreData();
                                                dialog.dismiss();
                                            }
                                        }).setCancelable(false).show();
                                    }

                                }

                                @Override
                                public void onError(@NotNull Throwable e) {
                                    progressBar.setIndeterminate(false);
                                    progressBar.setVisibility(View.GONE);
                                    linearLayoutError.setVisibility(View.VISIBLE);
                                    refreshButtonError.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            progressBar.setIndeterminate(true);
                                            progressBar.setVisibility(View.VISIBLE);
                                            linearLayoutError.setVisibility(View.GONE);
                                            getGenresList();
                                        }
                                    });
                                    Log.d("Check Your Internet", e.getMessage());
                                }

                                @Override
                                public void onComplete() {

                                }
                            })
            );
        } catch (Exception e) {
            Log.d("FetchGenresListService", e.getMessage());
        }
    }

}
