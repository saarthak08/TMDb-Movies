package com.sg.moviesindex.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GenresList implements Parcelable {

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
    public final static Parcelable.Creator<GenresList> CREATOR = new Creator<GenresList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GenresList createFromParcel(Parcel in) {
            return new GenresList(in);
        }

        public GenresList[] newArray(int size) {
            return (new GenresList[size]);
        }

    };

    protected GenresList(Parcel in) {
        in.readList(this.genres, (Genre.class.getClassLoader()));
    }

    public GenresList() {
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(genres);
    }

    public int describeContents() {
        return 0;
    }

}