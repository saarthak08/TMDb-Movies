package com.sg.moviesindex.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sg.moviesindex.BR;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "favourite_movies")
public class Movie extends BaseObservable implements Parcelable {

    @ColumnInfo(name = "imdb_id")
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    @Ignore
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = new ArrayList<>();


    @ColumnInfo(name = "budget")
    @SerializedName("budget")
    @Expose
    private Long budget;

    @ColumnInfo(name = "revenue")
    @SerializedName("revenue")
    @Expose
    private Long revenue;

    @ColumnInfo(name = "runtime")
    @SerializedName("runtime")
    @Expose
    private Long runtime;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    @Expose
    private String status;

    @ColumnInfo(name = "tagline")
    @SerializedName("tagline")
    @Expose
    private String tagline;


    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @ColumnInfo(name = "video")
    @SerializedName("video")
    @Expose
    private Boolean video;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    @Expose
    private Double popularity;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    @Expose
    private String posterPath;


    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @ColumnInfo(name = "genres")
    @SerializedName("genres")
    @Expose
    private ArrayList<Genre> genres = new ArrayList<>();


    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @ColumnInfo(name = "adult")
    @SerializedName("adult")
    @Expose
    private Boolean adult;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    @Expose
    private String overview;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @ColumnInfo(name = "casts_list")
    private ArrayList<Cast> castsList = new ArrayList<>();

    @ColumnInfo(name = "reviews_list")
    private ArrayList<Review> reviewsList = new ArrayList<>();


    @Ignore
    public final static Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {

        @Ignore
        @SuppressWarnings({
                "unchecked"
        })
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Ignore
        public Movie[] newArray(int size) {
            return (new Movie[size]);
        }

    };


    @Ignore
    protected Movie(Parcel in) {
        this.adult = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
        this.budget = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readTypedList(this.genres, (Genre.CREATOR));
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.imdbId = ((String) in.readValue((String.class.getClassLoader())));
        this.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
        this.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((Double) in.readValue((Double.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.revenue = ((Long) in.readValue((Long.class.getClassLoader())));
        this.runtime = ((Long) in.readValue((Long.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.tagline = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.video = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.voteAverage = ((Double) in.readValue((Double.class.getClassLoader())));
        in.readList(this.genreIds, (java.lang.Integer.class.getClassLoader()));
        in.readTypedList(castsList, Cast.CREATOR);
        in.readTypedList(reviewsList, Review.CREATOR);
    }


    @Ignore
    public Movie() {
    }

    public Movie(Boolean adult, String backdropPath, Long budget, ArrayList<Genre> genres, Long id, String imdbId, String originalLanguage, String originalTitle, String overview, Double popularity, String posterPath, String releaseDate, Long revenue, Long runtime, String status, String tagline, String title, Boolean video, Double voteAverage, ArrayList<Cast> castsList, ArrayList<Review> reviewsList) {
        super();
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.budget = budget;
        this.genres = genres;
        this.id = id;
        this.imdbId = imdbId;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.runtime = runtime;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.castsList = castsList;
        this.reviewsList = reviewsList;
    }


    @Ignore
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    @Ignore
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    @Bindable
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
        notifyPropertyChanged(BR.video);
    }

    @Bindable
    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;

        notifyPropertyChanged(BR.voteAverage);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
        notifyPropertyChanged(BR.popularity);
    }

    @Bindable
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        notifyPropertyChanged(BR.posterPath);
    }

    @Bindable
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        notifyPropertyChanged(BR.originalLanguage);
    }

    @Bindable
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        notifyPropertyChanged(BR.originalTitle);
    }


    @Bindable
    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        notifyPropertyChanged(BR.backdropPath);
    }

    @Bindable
    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
        notifyPropertyChanged(BR.adult);
    }

    @Bindable
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
        notifyPropertyChanged(BR.overview);
    }

    @Bindable
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        notifyPropertyChanged(BR.releaseDate);
    }

    @Bindable
    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
        notifyPropertyChanged(BR.imdbId);
    }

    @Bindable
    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
        notifyPropertyChanged(BR.budget);
    }

    @Bindable
    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
        notifyPropertyChanged(BR.revenue);
    }

    @Bindable
    public Long getRuntime() {
        return runtime;
    }

    public void setRuntime(Long runtime) {
        this.runtime = runtime;
        notifyPropertyChanged(BR.runtime);
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
        notifyPropertyChanged(BR.tagline);
    }

    @Bindable
    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
        notifyPropertyChanged(BR.genres);
    }

    @Bindable
    public ArrayList<Cast> getCastsList() {
        return castsList;
    }

    public void setCastsList(ArrayList<Cast> castsList) {
        this.castsList = castsList;
        notifyPropertyChanged(BR.castsList);
    }

    @Bindable
    public ArrayList<Review> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(ArrayList<Review> reviewsList) {
        this.reviewsList = reviewsList;
        notifyPropertyChanged(BR.reviewsList);
    }

    @Ignore
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adult);
        dest.writeValue(backdropPath);
        dest.writeValue(budget);
        dest.writeTypedList(genres);
        dest.writeValue(id);
        dest.writeValue(imdbId);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalTitle);
        dest.writeValue(overview);
        dest.writeValue(popularity);
        dest.writeValue(posterPath);
        dest.writeValue(releaseDate);
        dest.writeValue(revenue);
        dest.writeValue(runtime);
        dest.writeValue(status);
        dest.writeValue(tagline);
        dest.writeValue(title);
        dest.writeValue(video);
        dest.writeValue(voteAverage);
        dest.writeList(genreIds);
        dest.writeTypedList(castsList);
        dest.writeTypedList(reviewsList);
    }


    @Ignore
    public int describeContents() {
        return 0;
    }

    public static final DiffUtil.ItemCallback<Movie> callback = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.adult == newItem.adult &&
                    oldItem.video == newItem.video &&
                    oldItem.backdropPath.equals(newItem.backdropPath) &&
                    oldItem.id.equals(newItem.id) &&
                    oldItem.originalLanguage.equals(newItem.originalLanguage) &&
                    oldItem.originalTitle.equals(newItem.originalTitle) &&
                    oldItem.overview.equals(newItem.overview) &&
                    oldItem.popularity.equals(newItem.popularity) &&
                    oldItem.posterPath.equals(newItem.posterPath) &&
                    oldItem.releaseDate.equals(newItem.releaseDate) &&
                    oldItem.title.equals(newItem.title) &&
                    oldItem.voteAverage.equals(newItem.voteAverage) &&
                    oldItem.castsList.equals(newItem.castsList) &&
                    oldItem.reviewsList.equals(newItem.reviewsList);
        }
    };

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +

                '}';
    }
}
