package com.task.dawadoz.forecast.localDataProvider.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.task.dawadoz.forecast.localDataProvider.provider.SampleContentProvider.BASE_CONTENT_URI;
import static com.task.dawadoz.forecast.localDataProvider.provider.SampleContentProvider.PATH_FORECASTS;




@Entity(tableName = ForecastData.TABLE_NAME,
        indices = @Index(value = ForecastData.COLUMN_PARENT_CITY_ID),
        foreignKeys = @ForeignKey(entity = City.class,
                parentColumns = City.COLUMN_SERVER_ID,
                childColumns = ForecastData.COLUMN_PARENT_CITY_ID,
                onDelete = CASCADE))
public class ForecastData {


    // Content URI represents the base location for the table
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_FORECASTS).build();

    // These are special type prefixes that specify if a URI returns a list or a specific item
    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_FORECASTS;
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_FORECASTS;

    //    String used by SyncAdapter to specify which end point to go to
    public static final CharSequence SERVER_CALL = "forecast";

    public static final String TABLE_NAME = "forecast";

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_PARENT_CITY_ID = "city_ID";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = COLUMN_PARENT_CITY_ID)
    public Long parentCityId;


    public static final String COLUMN_TEMP = "temp";
    public static final String COLUMN_TEMP_MIN = "temp_min";
    public static final String COLUMN_TEMP_MAX = "temp_max";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String COLUMN_HUMIDITY = "humidity";

    public static final String COLUMN_WEATHER_MAIN = "main";
    public static final String COLUMN_WEATHER_DESC = "description";

    public static final String COLUMN_CLOUDS_ALL = "clouds_all";

    public static final String COLUMN_WIND_SPEED = "wind_speed";
    public static final String COLUMN_WIND_DEG = "wind_deg";

    public static final String COLUMN_DATE = "dt_txt";
    public static final String COLUMN_HOUR = "hr_txt";


    @ColumnInfo(name = COLUMN_TEMP)
    public String temp;

    @ColumnInfo(name = COLUMN_TEMP_MIN)
    public long tempMin;

    @ColumnInfo(name = COLUMN_TEMP_MAX)
    public long tempMax;


    @ColumnInfo(name = COLUMN_PRESSURE)
    public String pressure;


    @ColumnInfo(name = COLUMN_CLOUDS_ALL)
    public String cloud;

    @ColumnInfo(name = COLUMN_WEATHER_MAIN)
    public String weatherMain;

    @ColumnInfo(name = COLUMN_WEATHER_DESC)
    public String weatherDesc;

    @ColumnInfo(name = COLUMN_WIND_SPEED)
    public String windSpeed;

    @ColumnInfo(name = COLUMN_WIND_DEG)
    public String windDeg;

    @ColumnInfo(name = COLUMN_HUMIDITY)
    public String humidity;

    @ColumnInfo(name = COLUMN_DATE)
    public String date;

    @ColumnInfo(name = COLUMN_HOUR)
    public String hour;


    public static ForecastData fromContentValues(ContentValues values) {

        final ForecastData forecastData = new ForecastData();

//        basic
        if (values.containsKey(COLUMN_ID)) {
            forecastData.id = values.getAsLong(COLUMN_ID);
        }

        if (values.containsKey(COLUMN_PARENT_CITY_ID)) {
            forecastData.parentCityId = Long.valueOf(values.getAsString(COLUMN_PARENT_CITY_ID));
        }

        if (values.containsKey(COLUMN_TEMP)) {
            forecastData.temp = values.getAsString(COLUMN_TEMP);
        }

        if (values.containsKey(COLUMN_PRESSURE)) {
            forecastData.pressure = values.getAsString(COLUMN_PRESSURE);
        }

        if (values.containsKey(COLUMN_HUMIDITY)) {
            forecastData.humidity = values.getAsString(COLUMN_HUMIDITY);
        }

        if (values.containsKey(COLUMN_WEATHER_MAIN)) {
            forecastData.weatherMain = values.getAsString(COLUMN_WEATHER_MAIN);
        }

        if (values.containsKey(COLUMN_WEATHER_DESC)) {
            forecastData.weatherDesc = values.getAsString(COLUMN_WEATHER_DESC);
        }

        if (values.containsKey(COLUMN_CLOUDS_ALL)) {
            forecastData.cloud = values.getAsString(COLUMN_CLOUDS_ALL);
        }

        if (values.containsKey(COLUMN_WIND_SPEED)) {
            forecastData.windSpeed = values.getAsString(COLUMN_WIND_SPEED);
        }

        if (values.containsKey(COLUMN_WIND_DEG)) {
            forecastData.windDeg = values.getAsString(COLUMN_WIND_DEG);
        }

        if (values.containsKey(COLUMN_DATE)) {
            forecastData.date = values.getAsString(COLUMN_DATE);
        }

        if (values.containsKey(COLUMN_HOUR)) {
            forecastData.hour = values.getAsString(COLUMN_HOUR);
        }


        return forecastData;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getParentCityId() {
        return parentCityId;
    }

    public void setParentCityId(Long parentCityId) {
        this.parentCityId = parentCityId;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public long getTempMin() {
        return tempMin;
    }

    public void setTempMin(long tempMin) {
        this.tempMin = tempMin;
    }

    public long getTempMax() {
        return tempMax;
    }

    public void setTempMax(long tempMax) {
        this.tempMax = tempMax;
    }


    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }


    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(String windDeg) {
        this.windDeg = windDeg;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
