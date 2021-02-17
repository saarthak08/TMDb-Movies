package com.sg.moviesindex.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.sg.moviesindex.model.Movie;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FavouriteMoviesDAO {

    @Insert(onConflict = REPLACE)
    void insertFMovie(Movie movie);

    @Delete
    void deleteFMovie(Movie movie);

    @Query("select * from favourite_movies")
    LiveData<List<Movie>> getAllFMovies();

    @Query("select * from favourite_movies where title==:title")
    Movie getMovie(String title);


}
