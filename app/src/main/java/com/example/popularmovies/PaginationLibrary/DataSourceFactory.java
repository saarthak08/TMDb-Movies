package com.example.popularmovies.PaginationLibrary;

import android.app.Application;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.service.MovieDataService;

import androidx.lifecycle.MutableLiveData;

public class DataSourceFactory extends DataSource.Factory{
    private DataSource dataSource;
    private Application application;
    private MovieDataService movieDataService;
    private MutableLiveData<DataSource> dataSourceMutableLiveData;

    public DataSourceFactory(MovieDataService movieDataService,Application application) {
        this.application = application;
        this.movieDataService = movieDataService;
    }

    public DataSource create()
    {
        dataSource=new DataSource(movieDataService,application);
        dataSourceMutableLiveData.postValue(dataSource);
        return null;
    }

    public MutableLiveData<DataSource> getDataSourceMutableLiveData()
    {
        return dataSourceMutableLiveData;
    }
}