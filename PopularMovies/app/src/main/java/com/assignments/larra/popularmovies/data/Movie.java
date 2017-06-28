package com.assignments.larra.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by larra on 20/06/2017.
 */

public class Movie implements Parcelable{

    private String title;
    private String synopsis;
    private String bannerPath;
    private double rating;
    private String date;


    public Movie(){
        title = "default";
        synopsis = "";
        rating = 0.0f;
        date = "2016-10-19";
        bannerPath = "";
    }

    public Movie(String _title, String _synopsis, String _bannerPath, double _rating, String _date){
        title = _title;
        synopsis = _synopsis;
        rating = _rating;
        date = _date;
        bannerPath = _bannerPath;
    }

    private Movie(Parcel in){
        this.title = in.readString();
        this.synopsis = in.readString();
        this.bannerPath = in.readString();
        this.rating = in.readDouble();
        this.date = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(synopsis);
        parcel.writeString(bannerPath);
        parcel.writeDouble(rating);
        parcel.writeString(date);
    }

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
