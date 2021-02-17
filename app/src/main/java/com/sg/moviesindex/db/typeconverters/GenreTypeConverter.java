package com.sg.moviesindex.db.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.moviesindex.model.Genre;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GenreTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<Genre> gettingListFromString(String data) {
        if (data == null) {
            return new ArrayList<Genre>();
        }

        Type listType = new TypeToken<ArrayList<Genre>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String gettingStringFromList(ArrayList<Genre> someObjects) {
        return gson.toJson(someObjects);
    }

}

