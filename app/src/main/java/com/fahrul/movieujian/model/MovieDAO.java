package com.fahrul.movieujian.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDAO {
    @Query("SELECT * FROM TitelMovie")
    List<TitleMovie> getAll();

    @Query("SELECT * FROM TitelMovie WHERE title LIKE '%' || :title || '%'")
    List<TitleMovie> findByTitel(String title);

    @Insert
    void insertAll(TitleMovie... titleMovies);

    @Delete
    void deleteBiodata(TitleMovie titleMovie);

    @Update
    int updateBiodata(TitleMovie titleMovie);

}
