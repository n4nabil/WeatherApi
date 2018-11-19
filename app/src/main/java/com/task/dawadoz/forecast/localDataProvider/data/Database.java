package com.task.dawadoz.forecast.localDataProvider.data;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@android.arch.persistence.room.Database(entities = {City.class, ForecastData.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    //    sync adapter will be added
    private static Database sInstance;

    public static synchronized Database getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), Database.class, "N11")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
//            sInstance.populateInitialData();
        }
        return sInstance;
    }


    @SuppressWarnings("WeakerAccess")
    public abstract CityDAO cityDAO();

    public abstract ForecastDAO forecastDAO();


    private void populateInitialData() {
        if (cityDAO().count() == 0) {
            City city = new City();
            beginTransaction();

            for (int i = 0; i < 3; i++) {

                city.setName("London" + i);
                city.setTemp("20.1" + i);
                city.setServerID("524901");

                cityDAO().insert(city);
            }

            if (forecastDAO().count() == 0) {
                ForecastData forecastData = new ForecastData();
                beginTransaction();
                try {
                    for (int i = 0; i < 3; i++) {

                        forecastData.setParentCityId(Long.valueOf(524901));
                        forecastData.setTemp(String.valueOf(50));

                        forecastDAO().insert(forecastData);
                    }

                    setTransactionSuccessful();
                } finally {
                    endTransaction();
                }
            }
        }
    }
}
