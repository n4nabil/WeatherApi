package com.task.dawadoz.forecast.localDataProvider.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

@Dao
public interface ForecastDAO {


    @Query("SELECT COUNT(*) FROM " + ForecastData.TABLE_NAME)
    int count();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ForecastData forecastData);

    @Query("SELECT * FROM " + ForecastData.TABLE_NAME)
    Cursor selectAll();


    @Query("SELECT * FROM " + ForecastData.TABLE_NAME +" WHERE " + ForecastData.COLUMN_PARENT_CITY_ID + " = :id")
    Cursor selectSome(String id);


    @Query("SELECT *  FROM " + ForecastData.TABLE_NAME + " WHERE " + ForecastData.COLUMN_PARENT_CITY_ID + " = :id")
    Cursor selectById(long id);

    @Query("DELETE FROM " + ForecastData.TABLE_NAME + " WHERE " + ForecastData.COLUMN_ID + " = :id")
    int deleteById(long id);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(ForecastData forecastData);


    @Query("DELETE FROM " + ForecastData.TABLE_NAME)
    void removeAllForecastDatas();

}
