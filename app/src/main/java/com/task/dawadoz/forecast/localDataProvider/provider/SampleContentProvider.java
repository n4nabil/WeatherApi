/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.task.dawadoz.forecast.localDataProvider.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.task.dawadoz.forecast.localDataProvider.data.City;
import com.task.dawadoz.forecast.localDataProvider.data.CityDAO;
import com.task.dawadoz.forecast.localDataProvider.data.Database;
import com.task.dawadoz.forecast.localDataProvider.data.ForecastDAO;
import com.task.dawadoz.forecast.localDataProvider.data.ForecastData;

import java.util.ArrayList;

public class SampleContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.sync";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_CITIES = City.TABLE_NAME;
    public static final String PATH_FORECASTS = ForecastData.TABLE_NAME;


    private static final int CODE_CITY_TABLE = 1;
    private static final int CODE_CITY_RECORD = 2;

    private static final int CODE_FORECAST_TABLE = 5;
    private static final int CODE_FORECAST_RECORD = 6;


    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        MATCHER.addURI(AUTHORITY, City.TABLE_NAME, CODE_CITY_TABLE);
        MATCHER.addURI(AUTHORITY, City.TABLE_NAME + "/*", CODE_CITY_RECORD);

        MATCHER.addURI(AUTHORITY, ForecastData.TABLE_NAME, CODE_FORECAST_TABLE);
        MATCHER.addURI(AUTHORITY, ForecastData.TABLE_NAME + "/*", CODE_FORECAST_RECORD);

    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final Context context = getContext();
        final long id;
        switch (MATCHER.match(uri)) {
            case CODE_CITY_TABLE:
                if (context == null) {
                    return null;
                }
                id = Database.getInstance(context).cityDAO()
                        .insert(City.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                Log.d("AAA", "insert: " + id);
                return ContentUris.withAppendedId(uri, id);
            case CODE_CITY_RECORD:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);


            case CODE_FORECAST_TABLE:
                if (context == null) {
                    return null;
                }
                id = Database.getInstance(context).forecastDAO()
                        .insert(ForecastData.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                Log.d("AAA", "insert: " + id);
                return ContentUris.withAppendedId(uri, id);
            case CODE_FORECAST_RECORD:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortCity) {
        final int code = MATCHER.match(uri);
        if (code == CODE_CITY_TABLE || code == CODE_CITY_RECORD) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            CityDAO dao = Database.getInstance(context).cityDAO();
            final Cursor cursor;
            if (code == CODE_CITY_TABLE) {

                cursor = dao.selectAll();

            } else {
                cursor = dao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;

        } else if (code == CODE_FORECAST_TABLE || code == CODE_FORECAST_RECORD) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            ForecastDAO dao = Database.getInstance(context).forecastDAO();
            final Cursor cursor;
            if (code == CODE_FORECAST_TABLE) {
                if (selectionArgs !=null) {
                    cursor = dao.selectSome(selectionArgs[0]);
                } else {
                    cursor = dao.selectAll();
                }
            } else {
                cursor = dao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final Context context = getContext();
        final int count;
        switch (MATCHER.match(uri)) {
            case CODE_CITY_TABLE:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_CITY_RECORD:
                if (context == null) {
                    return 0;
                }
                count = Database.getInstance(context).cityDAO()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;

            case CODE_FORECAST_TABLE:
                if (context == null) {
                    return 0;
                }
                Database.getInstance(context).forecastDAO()
                        .removeAllForecastDatas();
                context.getContentResolver().notifyChange(uri, null);
                return 0;

            case CODE_FORECAST_RECORD:
                if (context == null) {
                    return 0;
                }
                count = Database.getInstance(context).forecastDAO()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_CITY_TABLE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + City.TABLE_NAME;
            case CODE_CITY_RECORD:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + City.TABLE_NAME;
            case CODE_FORECAST_TABLE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + ForecastData.TABLE_NAME;
            case CODE_FORECAST_RECORD:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + ForecastData.TABLE_NAME;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


//    @Override
//    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
//                      @Nullable String[] selectionArgs) {
//        final Context context = getContext();
//        final int count ;
//        switch (MATCHER.match(uri)) {
//            case CODE_CITY_TABLE:
//                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
//            case CODE_CITY_RECORD:
//                if (context == null) {
//                    return 0;
//                }
////                final City city = City.fromContentValues(values);
////                city.setId(ContentUris.parseId(uri));
//                count = Database.getInstance(context).cityDAO()
//                        .updateValues(values.getAsString(City.COLUMN_STATUS),values.getAsString(City.COLUMN_NUMBER_OF_PIECES),values.getAsLong(City.COLUMN_ID));
//                context.getContentResolver().notifyChange(uri, null);
//                return count;
//
//            case CODE_FORECAST_TABLE:
//                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
//            case CODE_FORECAST_RECORD:
//                if (context == null) {
//                    return 0;
//                }
////                final City city = City.fromContentValues(values);
//                final ForecastData awb = ForecastData.fromContentValues(values);
//                awb.setId(ContentUris.parseId(uri));
//                count = Database.getInstance(context).forecastDAO()
//                        .update(awb);
//                context.getContentResolver().notifyChange(uri, null);
//                return count;
//
//
//
//
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//    }


//
//    @Override
//    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
//            @Nullable String[] selectionArgs) {
//        switch (MATCHER.match(uri)) {
//            case CODE_CITY_TABLE:
//                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
//            case CODE_CITY_RECORD:
//                final Context context = getContext();
//                if (context == null) {
//                    return 0;
//                }
//                final Cheese cheese = Cheese.fromContentValues(values);
//                cheese.id = ContentUris.parseId(uri);
//                final int count = Database.getInstance(context).cheese()
//                        .update(cheese);
//                context.getContentResolver().notifyChange(uri, null);
//                return count;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final Database database = Database.getInstance(context);
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

}
