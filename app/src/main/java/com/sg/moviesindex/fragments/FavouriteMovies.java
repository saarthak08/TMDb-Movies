package com.sg.moviesindex.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sg.moviesindex.R;
import com.sg.moviesindex.adapter.MoviesAdapter;
import com.sg.moviesindex.databinding.FragmentFavouriteMoviesBinding;
import com.sg.moviesindex.model.Movie;
import com.sg.moviesindex.viewmodel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMovies extends Fragment {


    private ArrayList<Movie> movie;
    private Context context;
    private FragmentFavouriteMoviesBinding fragmentFavouriteMoviesBinding;

    public FavouriteMovies() {

    }

    public static FavouriteMovies newInstance() {
        FavouriteMovies fragment = new FavouriteMovies();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFavouriteMoviesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_movies, container, false);
        return fragmentFavouriteMoviesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Favourite Movies");
        context = getContext();
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getAllMovies().observe(getActivity(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movie = (ArrayList<Movie>) movies;
                showRecyclerView();
            }
        });
    }

    private void showRecyclerView() {
        TextView textView = fragmentFavouriteMoviesBinding.tvNoMovies;
        if (movie.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        RecyclerView recyclerView = fragmentFavouriteMoviesBinding.rvF4;
        MoviesAdapter moviesAdapter = new MoviesAdapter(context, movie);
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        } else {
            recyclerView.setLayoutManager((new GridLayoutManager(context, 4)));
        }
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        moviesAdapter.notifyDataSetChanged();
    }
}

