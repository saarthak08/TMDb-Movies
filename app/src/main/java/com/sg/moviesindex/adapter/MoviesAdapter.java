package com.sg.moviesindex.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sg.moviesindex.R;
import com.sg.moviesindex.databinding.MovieListItemBinding;
import com.sg.moviesindex.model.Movie;
import com.sg.moviesindex.view.MoviesInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Movie> movies;
    private static int VIEW_TYPE_ITEM = 0;
    private static int VIEW_TYPE_LOADING = 1;

    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.loading)
                .into(view);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            MovieListItemBinding movieListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                    R.layout.movie_list_item, viewGroup, false);
            return new MoviesViewHolder(movieListItemBinding);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loadmore_progressbar, viewGroup, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MoviesViewHolder) {
            Movie movie = movies.get(i);
            try {
                Date date1 = null;
                if (!movie.getReleaseDate().contains(",")) {
                    date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(movie.getReleaseDate());
                    DateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                    movie.setReleaseDate(format.format(date1));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            ((MoviesViewHolder) viewHolder).movieListItemBinding.setMovie(movie);
        } else if (viewHolder instanceof LoadingViewHolder) {
        }
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        private MovieListItemBinding movieListItemBinding;

        MoviesViewHolder(@NonNull final MovieListItemBinding movieListItemBinding) {
            super(movieListItemBinding.getRoot());
            this.movieListItemBinding = movieListItemBinding;
            movieListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Movie movie = movies.get(position);
                        Intent i = new Intent(context, MoviesInfo.class);
                        i.putExtra("movie", movie);
                        context.startActivity(i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return movies.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadmpre_progressbar);
        }
    }

}