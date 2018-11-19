package com.task.dawadoz.forecast.localDataProvider.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import static com.task.dawadoz.forecast.localDataProvider.provider.SampleContentProvider.BASE_CONTENT_URI;
import static com.task.dawadoz.forecast.localDataProvider.provider.SampleContentProvider.PATH_CITIES;


@Entity(tableName = City.TABLE_NAME,
        primaryKeys = {City.COLUMN_ID, City.COLUMN_SERVER_ID},
        indices={
                @Index(value=City.COLUMN_ID),
                @Index(value=City.COLUMN_SERVER_ID, unique = true)
        }
)
public class City {

    // Content URI represents the base location for the table
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_CITIES).build();

    // These are special type prefixes that specify if a URI returns a list or a specific item
    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_CITIES;
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CITIES;

//    String used by SyncAdapter to specify which end point to go to
    public static final CharSequence SERVER_CALL = "cities";


    public static final String TABLE_NAME = "cities";

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_SERVER_ID = "server_ID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TEMP = "temp";


//    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    private long id;

//    @PrimaryKey
    @NonNull
    @ColumnInfo(name = COLUMN_SERVER_ID)
    private String serverID;

    @ColumnInfo(name = COLUMN_NAME)
    private String name;


    @ColumnInfo(name = COLUMN_TEMP)
    private String temp;


    public City() {
    }

    public static City fromContentValues(ContentValues values) {

        final City city = new City();

//        basic
        if (values.containsKey(COLUMN_ID)) {
            city.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_SERVER_ID)) {
            city.serverID = values.getAsString(COLUMN_SERVER_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            city.name = values.getAsString(COLUMN_NAME);
        }
        if (values.containsKey(COLUMN_TEMP)) {
            city.temp = values.getAsString(COLUMN_TEMP);
        }

        return city;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
