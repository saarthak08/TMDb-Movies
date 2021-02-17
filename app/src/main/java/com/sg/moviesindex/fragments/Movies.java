package com.sg.moviesindex.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sg.moviesindex.R;
import com.sg.moviesindex.adapter.MoviesAdapter;
import com.sg.moviesindex.databinding.FragmentMoviesBinding;
import com.sg.moviesindex.model.Movie;
import com.sg.moviesindex.service.FetchFirstTimeDataService;
import com.sg.moviesindex.service.FetchMoreDataService;
import com.sg.moviesindex.service.network.RetrofitInstance;
import com.sg.moviesindex.utils.PaginationScrollListener;
import com.sg.moviesindex.utils.SearchUtil;
import com.sg.moviesindex.view.MainActivity;
import com.sg.moviesindex.viewmodel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;


public class Movies extends Fragment {

    private static final String firstTimeDataFetchArg = "firstTimeDataFetch";


    public Movies() {
        //Required Constructor
    }

    public static Movies newInstance(FetchFirstTimeDataService fetchFirstTimeDataService, SearchUtil searchUtil) {
        Movies fragment = new Movies();
        Bundle args = new Bundle();
        args.putParcelable(firstTimeDataFetchArg, fetchFirstTimeDataService);
        fragment.setArguments(args);
        return fragment;
    }


    private MoviesAdapter moviesAdapter;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentMoviesBinding fragmentMoviesBinding;
    private PaginationScrollListener paginationScrollListener;
    GridLayoutManager gridLayoutManager;
    private static CompositeDisposable compositeDisposable;
    private static final long genreid = MainActivity.genreid;
    private FetchMoreDataService fetchMoreDataService;
    private SearchUtil searchUtil;
    private FetchFirstTimeDataService firstTimeData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if (getArguments() != null) {
            firstTimeData = (FetchFirstTimeDataService) getArguments().get(firstTimeDataFetchArg);
            searchUtil=firstTimeData.searchUtil;
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMoviesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false);
        return fragmentMoviesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        RecyclerView recyclerView = fragmentMoviesBinding.rv2;
        compositeDisposable = new CompositeDisposable();
        swipeRefreshLayout = fragmentMoviesBinding.swiperefresh2;
        if (MainActivity.drawer == 0) {
            requireActivity().setTitle("Popular Movies");
        } else if (MainActivity.drawer == 3) {
            requireActivity().setTitle("Top Rated Movies");
        } else if (MainActivity.drawer == 4) {
            if (MainActivity.genres != null && MainActivity.genres.size() != 0) {
                requireActivity().setTitle("Genre: " + MainActivity.genres.get(MainActivity.selected).getName());
            }
        } else if (MainActivity.drawer == 6) {
            requireActivity().setTitle("Search Results: " + MainActivity.queryM);
        } else if (MainActivity.drawer == 2) {
            requireActivity().setTitle("Upcoming Movies: " + MainActivity.region);
        } else if (MainActivity.drawer == 1) {
            requireActivity().setTitle("Now Playing: " + MainActivity.region);
        }
        context = getContext();
        ArrayList<Movie> movieList = MainActivity.movieList;
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.DKGRAY, Color.RED, Color.GREEN, Color.MAGENTA, Color.BLACK, Color.CYAN);
        moviesAdapter = new MoviesAdapter(context, movieList);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    RetrofitInstance.resetCache();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < requireActivity().getSupportFragmentManager().getBackStackEntryCount(); i++) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
                if (MainActivity.drawer != 4 && MainActivity.drawer != 6) {
                    firstTimeData.getDataFirst();
                } else if (MainActivity.drawer == 4) {
                    firstTimeData.fetchGenresListService.getGenresList();
                } else if (MainActivity.drawer == 6) {
                    MainActivity.drawer = 0;
                    firstTimeData.getDataFirst();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (moviesAdapter.getItemViewType(position)) {
                    case 0:
                        return 1;
                    case 1:
                        return 2;
                    default:
                        return -1;
                }
            }
        });
        gridLayoutManager.scrollToPosition(0);
        gridLayoutManager.scrollToPositionWithOffset(0, 0);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        moviesAdapter.notifyDataSetChanged();
        fetchMoreDataService = new FetchMoreDataService(recyclerView, movieList, compositeDisposable, getActivity(), moviesAdapter);
        try {
            paginationScrollListener = new PaginationScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (MainActivity.drawer == 0 || MainActivity.drawer == 1 || MainActivity.drawer == 2 || MainActivity.drawer == 3) {
                        if ((page + 1) <= MainActivity.totalPages) {
                            fetchMoreDataService.loadMore(MainActivity.drawer, page + 1);
                        }
                    } else if (MainActivity.drawer == 4) {
                        if ((page + 1) <= MainActivity.totalPagesGenres) {
                            fetchMoreDataService.loadMoreGenres(page + 1);
                        }
                    } else if (MainActivity.drawer == 6) {
                        if ((page + 1) <= MainActivity.totalPages) {
                            searchUtil.loadMoreSearches(page + 1, MainActivity.queryM);
                        }
                    }
                }
            };
        } catch (Error e) {
            Log.d("LoadMore", e.getMessage());
            // Toast.makeText(getContext(),"Error in Loading Movies!",Toast.LENGTH_SHORT).show();
        }
        recyclerView.addOnScrollListener(paginationScrollListener);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}

