package com.sg.moviesindex.db.typeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sg.moviesindex.model.Cast;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CastTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<Cast> gettingListFromString(String data) {
        if (data == null) {
            return new ArrayList<Cast>();
        }

        Type listType = new TypeToken<ArrayList<Cast>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String gettingStringFromList(ArrayList<Cast> someObjects) {
        return gson.toJson(someObjects);
    }

}
