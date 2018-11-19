package com.task.dawadoz.forecast.localDataProvider.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

@Dao
public interface CityDAO {

    @Query("SELECT COUNT(*) FROM " + City.TABLE_NAME)
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(City city);

    @Query("SELECT * FROM " + City.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT *  FROM " + City.TABLE_NAME + " WHERE " + City.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("DELETE FROM " + City.TABLE_NAME + " WHERE " + City.COLUMN_ID + " = :id")
    int deleteById(long id);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(City city);


    @Query("DELETE FROM " + City.TABLE_NAME)
    void removeAllCities();


}
