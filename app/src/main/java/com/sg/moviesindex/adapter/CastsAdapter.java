package com.sg.moviesindex.adapter;


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
import com.sg.moviesindex.databinding.CastListItemBinding;
import com.sg.moviesindex.model.Cast;
import com.sg.moviesindex.model.CastsList;


public class CastsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private CastsList castsList;
    private static int VIEW_TYPE_ITEM = 0;
    private static int VIEW_TYPE_LOADING = 1;

    public CastsAdapter(CastsList castsList) {
        this.castsList = castsList;
    }

    @BindingAdapter({"profileUrl"})
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
            CastListItemBinding castListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                    R.layout.cast_list_item, viewGroup, false);
            return new CastsViewHolder(castListItemBinding);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loadmore_progressbar, viewGroup, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CastsViewHolder) {
            Cast cast = castsList.getCast().get(i);
            ((CastsViewHolder) viewHolder).castListItemBinding.setCast(cast);
            ((CastsViewHolder) viewHolder).castListItemBinding.setGender(cast.getGender() == 1 ? "Female" : cast.getGender() == 2 ? "Male" : "Unknown");
        } else if (viewHolder instanceof LoadingViewHolder) {
        }
    }

    class CastsViewHolder extends RecyclerView.ViewHolder {
        private CastListItemBinding castListItemBinding;

        CastsViewHolder(@NonNull final CastListItemBinding castListItemBinding) {
            super(castListItemBinding.getRoot());
            this.castListItemBinding = castListItemBinding;
        }
    }

    @Override
    public int getItemCount() {
        return castsList == null || castsList.getCast() == null ? 0 : castsList.getCast().size();
    }

    @Override
    public int getItemViewType(int position) {
        return castsList.getCast().get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadmpre_progressbar);
        }
    }

}