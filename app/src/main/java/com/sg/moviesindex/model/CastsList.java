package com.sg.moviesindex.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CastsList implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("cast")
    @Expose
    private List<Cast> cast = null;
    public final static Parcelable.Creator<CastsList> CREATOR = new Creator<CastsList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CastsList createFromParcel(Parcel in) {
            return new CastsList(in);
        }

        public CastsList[] newArray(int size) {
            return (new CastsList[size]);
        }

    };
    private final static long serialVersionUID = -6389819486142662649L;

    protected CastsList(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.cast, (Cast.class.getClassLoader()));
    }

    public CastsList() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(cast);
    }

    public int describeContents() {
        return 0;
    }

}