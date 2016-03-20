package com.arunsandip.myapplication.Util;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arun on 3/18/2016.
 */
public class MovieSearchPojo {

    @SerializedName("Title")
    private   String title;
    @SerializedName("Year")
    private String year;
    private  String imdbID;
    @SerializedName("Type")
    private  String type;
    @SerializedName("Poster")
    private String poster;

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getTitle() {

        return title;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
