package com.sg.moviesindex.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import com.sg.moviesindex.R;
import com.sg.moviesindex.adapter.CastsAdapter;
import com.sg.moviesindex.adapter.ReviewsAdapter;
import com.sg.moviesindex.config.BuildConfigs;
import com.sg.moviesindex.databinding.ActivityMoviesInfoBinding;
import com.sg.moviesindex.model.Cast;
import com.sg.moviesindex.model.CastsList;
import com.sg.moviesindex.model.Genre;
import com.sg.moviesindex.model.Movie;
import com.sg.moviesindex.model.Review;
import com.sg.moviesindex.model.ReviewsList;
import com.sg.moviesindex.service.network.RetrofitInstance;
import com.sg.moviesindex.service.network.TMDbService;
import com.sg.moviesindex.utils.PaginationScrollListener;
import com.sg.moviesindex.viewmodel.MainViewModel;
import com.varunest.sparkbutton.SparkButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MoviesInfo extends AppCompatActivity {
    private Movie movie;
    private Boolean bool;
    private ActivityMoviesInfoBinding activityMoviesInfoBinding;
    private MainViewModel mainViewModel;
    public static final String PROGRESS_UPDATE = "progress_update";
    private TMDbService tMDbService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    final String ApiKey = BuildConfigs.apiKey;
    private ReviewsAdapter reviewsAdapter;
    private final ReviewsList reviews = new ReviewsList();
    private CastsList casts = new CastsList();
    private SparkButton sparkButton;
    private LinearLayoutManager linearLayoutManagerReviews;
    private PaginationScrollListener paginationScrollListenerReviews;
    private RecyclerView recyclerViewReviews;
    private RecyclerView recyclerViewCasts;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        View parentLayout = findViewById(android.R.id.content);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        tMDbService = RetrofitInstance.getTMDbService(MoviesInfo.this);
        activityMoviesInfoBinding = DataBindingUtil.setContentView(MoviesInfo.this, R.layout.activity_movies_info);
        linearLayoutManagerReviews = new LinearLayoutManager(MoviesInfo.this);
        reviewsAdapter = new ReviewsAdapter(reviews);
        reviews.setResults(new ArrayList<Review>());
        casts.setCast(new ArrayList<Cast>());
        reviews.setTotalPages(1);
        recyclerViewReviews = activityMoviesInfoBinding.secondaryLayout.rvReviews;
        recyclerViewReviews.setLayoutManager(linearLayoutManagerReviews);
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCasts = activityMoviesInfoBinding.secondaryLayout.rvCasts;
        LinearLayoutManager linearLayoutManagerCasts = new LinearLayoutManager(MoviesInfo.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCasts.setLayoutManager(linearLayoutManagerCasts);
        recyclerViewCasts.setItemAnimator(new DefaultItemAnimator());
        Intent i = getIntent();
        if (i.hasExtra("movie")) {
            movie = i.getParcelableExtra("movie");
            bool = i.getBooleanExtra("boolean", false);
            if (MainActivity.imageup <= 2) {
                Snackbar.make(parentLayout, "Swipe Image Up For More Information!", Snackbar.LENGTH_SHORT).show();
                MainActivity.imageup++;
            }
            if (mainViewModel.getMovie(movie.getTitle()) != null) {
                activityMoviesInfoBinding.secondaryLayout.sparkButton.setChecked(true);
                activityMoviesInfoBinding.secondaryLayout.sparkButton.setActiveImage(R.drawable.ic_heart_on);
            } else {
                activityMoviesInfoBinding.secondaryLayout.sparkButton.setChecked(false);
                activityMoviesInfoBinding.secondaryLayout.sparkButton.setInactiveImage(R.drawable.ic_heart_off);
            }
            activityMoviesInfoBinding.setMovie(movie);
            activityMoviesInfoBinding.secondaryLayout.setLocale(new Locale(movie.getOriginalLanguage()).getDisplayLanguage(Locale.ENGLISH));
            chipGroup = activityMoviesInfoBinding.secondaryLayout.chipGroup;
        }
        getFullInformation();
        getParcelableData();
        setPaginationListeners();
        setProgressBar();
        getReviews(1);
        getCasts();

        activityMoviesInfoBinding.secondaryLayout.sparkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SparkButton) v).isChecked()) {
                    mainViewModel.DeleteMovie(movie);
                    activityMoviesInfoBinding.secondaryLayout.sparkButton.playAnimation();
                    Snackbar.make(v, "Unmarked as Favourite", Snackbar.LENGTH_SHORT).show();
                    activityMoviesInfoBinding.secondaryLayout.sparkButton.setInactiveImage(R.drawable.ic_heart_off);
                    activityMoviesInfoBinding.secondaryLayout.sparkButton.setChecked(false);
                } else {
                    ArrayList<Cast> arrCasts = new ArrayList<Cast>(casts.getCast());
                    movie.setCastsList(arrCasts);
                    ArrayList<Review> arrReviews = new ArrayList<Review>(reviews.getResults());
                    movie.setReviewsList(arrReviews);
                    mainViewModel.AddMovie(movie);
                    Snackbar.make(v, "Marked as Favourite", Snackbar.LENGTH_SHORT).show();
                    activityMoviesInfoBinding.secondaryLayout.sparkButton.playAnimation();
                    activityMoviesInfoBinding.secondaryLayout.sparkButton.setInactiveImage(R.drawable.ic_heart_on);
                    activityMoviesInfoBinding.secondaryLayout.sparkButton.setChecked(true);

                }
            }
        });
    }

    public void setProgressBar() {
        CircularProgressIndicator circleProgressBar = activityMoviesInfoBinding.secondaryLayout.circularProgress;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circleProgressBar.setProgress(movie.getVoteAverage(), 10.0);
            }
        }, 1500);
    }

    public void getFullInformation() {
        Observable<Movie> movieObservable = tMDbService.getFullMovieInformation(movie.getId(), BuildConfigs.apiKey);
        compositeDisposable.add(movieObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<Movie>() {
            @Override
            public void onNext(Movie moviex) {
                if (moviex != null) {
                    movie = moviex;
                    Date date1 = null;
                    try {
                        date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(movie.getReleaseDate());
                        DateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                        movie.setReleaseDate(format.format(date1));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    activityMoviesInfoBinding.setMovie(movie);
                    for (Genre x : movie.getGenres()) {
                        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_layout_item, chipGroup, false);
                        chip.setText(x.getName());
                        chipGroup.addView(chip);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        }));
    }

    public void setPaginationListeners() {
        paginationScrollListenerReviews = new PaginationScrollListener(linearLayoutManagerReviews) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if ((page + 1) <= reviews.getTotalPages()) {
                    getReviews(page + 1);
                }
            }
        };
    }

    public void getParcelableData() {
        Intent i = getIntent();
        if (i.hasExtra("movie")) {
            movie = i.getParcelableExtra("movie");
            if (movie.getCastsList() != null) {
                casts.setCast(movie.getCastsList());
            }
            if (movie.getReviewsList() != null) {
                reviews.setResults(movie.getReviewsList());
            }
            recyclerViewReviews.setAdapter(reviewsAdapter);
            CastsAdapter castsAdapter = new CastsAdapter(casts);
            recyclerViewCasts.setAdapter(castsAdapter);
        }
    }

    public void getCasts() {
        Observable<CastsList> castsList = tMDbService.getCasts(movie.getId(), ApiKey).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                getParcelableData();
            }
        });
        compositeDisposable.add(castsList.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                new DisposableObserver<CastsList>() {
                    @Override
                    public void onNext(CastsList castsList) {
                        if (castsList != null && castsList.getCast() != null) {
                            casts = castsList;
                            CastsAdapter castsAdapter = new CastsAdapter(casts);
                            recyclerViewCasts.setAdapter(castsAdapter);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                }
        ));
    }

    public void getReviews(int pageNo) {
        Observable<ReviewsList> reviewsList = tMDbService.getReviews(movie.getId(), ApiKey, pageNo).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                getParcelableData();
            }
        });
        recyclerViewReviews.setAdapter(reviewsAdapter);
        recyclerViewReviews.addOnScrollListener(paginationScrollListenerReviews);
        compositeDisposable.add(reviewsList.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ReviewsList>() {
                    @Override
                    public void onNext(ReviewsList reviewsList) {
                        if (reviewsList != null && reviewsList.getResults() != null) {
                            reviews.setTotalPages(reviewsList.getTotalPages());
                            reviews.setPage(reviewsList.getPage());
                            reviews.setId(reviewsList.getId());
                            reviews.setTotalResults(reviewsList.getTotalResults());
                            for (Review review : reviewsList.getResults()) {
                                reviews.getResults().add(review);
                                reviewsAdapter.notifyItemInserted(reviews.getResults().size() - 1);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();

    }

}
