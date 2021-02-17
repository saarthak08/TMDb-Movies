package com.sg.moviesindex.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiscoversList implements Parcelable {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<Discover> discovers = null;
    public final static Parcelable.Creator<DiscoversList> CREATOR = new Creator<DiscoversList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DiscoversList createFromParcel(Parcel in) {
            return new DiscoversList(in);
        }

        public DiscoversList[] newArray(int size) {
            return (new DiscoversList[size]);
        }

    };

    protected DiscoversList(Parcel in) {
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalResults = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.discovers, (Discover.class.getClassLoader()));
    }

    public DiscoversList() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Discover> getResults() {
        return discovers;
    }

    public void setResults(List<Discover> discovers) {
        this.discovers = discovers;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeValue(totalResults);
        dest.writeValue(totalPages);
        dest.writeList(discovers);
    }

    public int describeContents() {
        return 0;
    }

}
