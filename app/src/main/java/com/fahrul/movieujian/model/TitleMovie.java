package com.fahrul.movieujian.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "TitelMovie")
public class TitleMovie
{


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "rating")
    private String rating;
    @ColumnInfo(name = "genre")
    private String genre;
    @ColumnInfo (name = "directeBy")
    private String directedBy;
    @ColumnInfo (name = "writtenBy")
    private String writtenBy;
    @ColumnInfo(name = "inTheater")
    private String inTheater;
    @ColumnInfo(name = "studio")
    private String studio;
    @ColumnInfo (name = "imgPoster")
    private String imgPoster;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirectedBy() {
        return directedBy;
    }

    public void setDirectedBy(String directedBy) {
        this.directedBy = directedBy;
    }

    public String getWrittenBy() {
        return writtenBy;
    }

    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }

    public String getInTheater() {
        return inTheater;
    }

    public void setInTheater(String inTheater) {
        this.inTheater = inTheater;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getImgPoster() {
        return imgPoster;
    }

    public void setImgPoster(String imgPoster) {
        this.imgPoster = imgPoster;
    }
}