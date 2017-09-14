package com.duniyatv.duniyamovies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashgarhussain on 7/30/17.
 *
 * This class represents the movie DAO/Model
 */

public class Movie {
    private String releaseDate;
    private String posterPath;
    private String plotSynopsis;
    private double rating;
    private String title;
    private long id;
    private String review;
    private ArrayList<String> trailersList;

    public Movie(){
        ArrayList trailersList = new ArrayList();
    }

    public Movie(String releaseDate, String posterPath, String plotSynopsis, double rating,
                 String title, long id){
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.plotSynopsis = plotSynopsis;
        this.rating = rating;
        this.title = title;
        this.id = id;
    }

    public Movie(String posterPath){this.posterPath = posterPath;}

    // Ye olde getters and setters
    public String getPosterPath(){return posterPath;}
    public void setPosterPath(String posterPath){this.posterPath = posterPath;}
    public String getPlotSynopsis() {
        return plotSynopsis;
    }
    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public String getReleaseDate() {
      return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {this.releaseDate = releaseDate;}
    public int describeContents(){
        return this.hashCode();
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setId(long id) {this.id = id;};
    public long getId() {return this.id;};
    public void setReview(String review){this.review = review;}
    public String getReview(){return this.review;}
    public ArrayList getTrailersList(){return this.trailersList;}
    public void addTrailer(String key){
        if (trailersList == null) trailersList = new ArrayList<String>();
        trailersList.add(key);
    }
}