package com.fahrul.movieujian.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface MovieDAO {
    @Query("SELECT * FROM titelMovie ")
    List<TitleMovie> getAll();

    @Query("SELECT * FROM  titelMovie WHERE title LIKE '%' || :title || '%' ")
    List<TitleMovie>findByTitle(String title);

    @Insert
    void insertAll(TitleMovie... users);

    @Delete
    void deletTitelMovie(TitleMovie titleMovie);

    @Update
    int updateTitleMovie(TitleMovie titleMovie);
    
}
