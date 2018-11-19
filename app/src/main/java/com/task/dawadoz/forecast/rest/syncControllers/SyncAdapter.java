package com.task.dawadoz.forecast.rest.syncControllers;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.task.dawadoz.forecast.localDataProvider.data.City;
import com.task.dawadoz.forecast.localDataProvider.data.ForecastData;
import com.task.dawadoz.forecast.localDataProvider.provider.SampleContentProvider;
import com.task.dawadoz.forecast.rest.net.APIService;
import com.task.dawadoz.forecast.rest.net.ApiUtils;
import com.task.dawadoz.forecast.rest.serverModels.BulkCitiesResponse;
import com.task.dawadoz.forecast.rest.serverModels.BulkDaysResponse;
import com.task.dawadoz.forecast.rest.serverModels.innerModels.List;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is used by the Android framework to perform synchronization. IMPORTANT: do NOT create
 * new Threads to perform logic, Android will do this for you; hence, the name.
 * <p>
 * The goal here to perform synchronization, is to do it efficiently as possible. We use some
 * ContentProvider features to batch our writes to the local data source. Be sure to handle all
 * possible exceptions accordingly; random crashes is not a good user-experience.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SYNC_ADAPTER";
    private static final String BUNDLE_KEY = "key";
    private static final String CITY_SERVER_ID_KEY = "cityID";

    /**
     * This gives us access to our local data source.
     */
    private final ContentResolver resolver;
    private APIService apiService = ApiUtils.getAPIService();


    public SyncAdapter(Context c, boolean autoInit) {
        this(c, autoInit, false);
    }

    public SyncAdapter(Context c, boolean autoInit, boolean parallelSync) {
        super(c, autoInit, parallelSync);
        this.resolver = c.getContentResolver();
    }


    /**
     * Manual force Android to perform a sync with our SyncAdapter.
     */
    public static void performSync() {
        Bundle b = new Bundle();

        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(AccountGeneral.getAccount(),
                SampleContentProvider.AUTHORITY, b);
    }

    public static void performSync(String cityServerId) {
        Bundle b = new Bundle();

        if (cityServerId != null) {
            b.putCharSequence(CITY_SERVER_ID_KEY, cityServerId);
            Log.d(TAG, "performSync: "+ cityServerId);
        }else {
            b.putCharSequence(CITY_SERVER_ID_KEY, null);
            Log.d(TAG, "performSync: "+ "524901 MM" );
        }

        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(AccountGeneral.getAccount(),
                SampleContentProvider.AUTHORITY, b);

    }

    /**
     * This method is run by the Android framework, on a new Thread, to perform a sync.
     *
     * @param account    Current account
     * @param extras     Bundle extras
     * @param authority  Content authority
     * @param provider   {@link ContentProviderClient}
     * @param syncResult Object to write stats to
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.w(TAG, "Starting synchronization...  ");

        try {

            if (extras.getCharSequence(CITY_SERVER_ID_KEY)!= null) {
                // Synchronize our news feed
                Log.d(TAG, "onPerformSync1: "+extras.getCharSequence(CITY_SERVER_ID_KEY));
                syncNewsForecast(syncResult, String.valueOf(extras.getCharSequence(CITY_SERVER_ID_KEY)));
            } else {
                Log.d(TAG, "onPerformSync2: "+extras.getCharSequence(CITY_SERVER_ID_KEY));
                syncNewsCities(syncResult);

            }






        } catch (IOException ex) {
            Log.e(TAG, "Error synchronizing!", ex);
            syncResult.stats.numIoExceptions++;
        } catch (JSONException ex) {
            Log.e(TAG, "Error synchronizing!", ex);
            syncResult.stats.numParseExceptions++;
        } catch (RemoteException | OperationApplicationException ex) {
            Log.e(TAG, "Error synchronizing!", ex);
            syncResult.stats.numAuthExceptions++;
        }

        Log.w(TAG, "Finished synchronization!");
    }

    /**
     * Performs synchronization of our pretend news feed source.
     *
     * @param syncResult Write our stats to this
     */
    private void syncNewsCities(final SyncResult syncResult)
            throws IOException, JSONException, RemoteException, OperationApplicationException {

        apiService.getDataForSeveralCities().enqueue(new Callback<BulkCitiesResponse>() {
            @Override
            public void onResponse(Call<BulkCitiesResponse> call, Response<BulkCitiesResponse> response) {
                if (response.code() == 200) {

                    java.util.List<List> inComingList = response.body().getList();

                    // We need to collect all the network items in a hash table
                    Log.i(TAG, "Fetching server entries... " + inComingList.size());
                    Map<String, List> networkEntries = new HashMap<>();

                    for (int i = 0; i < inComingList.size(); i++) {
                        networkEntries.put(String.valueOf(inComingList.get(i).getId()), inComingList.get(i));

                    }

                    updateLocalDataBaseWithNewCities(networkEntries, syncResult);


                } else {
                    Log.i(TAG, "response code =" + response.code());

                }

            }

            @Override
            public void onFailure(Call<BulkCitiesResponse> call, Throwable t) {
                Log.i(TAG, "response :" + t.getMessage());

            }
        });


    }


    private void updateLocalDataBaseWithNewCities(Map<String, List> networkEntries, SyncResult syncResult) {


        // Create list for batching ContentProvider transactions
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();


        // Compare the hash table of network entries to all the local entries
        Log.i(TAG, "Fetching local entries...");

        Cursor c = resolver.query(City.CONTENT_URI,
                null, null, null, null, null);


        Log.i(TAG, "Fetching local entries..." + c.getColumnCount());

        assert c != null;
        c.moveToFirst();

        String cityServerID, cityTemp, cityID;
        com.task.dawadoz.forecast.rest.serverModels.innerModels.List foundInNewEntries = null;

        for (int i = 0; i < c.getCount(); i++) {
            syncResult.stats.numEntries++;

            // Create local article entry
            cityServerID = c.getString(c.getColumnIndex(City.COLUMN_SERVER_ID));
            cityID = c.getString(c.getColumnIndex(City.COLUMN_ID));

            // Try to retrieve the local entry from network entries
            foundInNewEntries = networkEntries.get(cityServerID);

            if (foundInNewEntries != null) {
                // The entry exists, remove from hash table to prevent re-inserting it
                networkEntries.remove(cityServerID);

                cityTemp = c.getString(c.getColumnIndex(City.COLUMN_TEMP));
                // Check to see if it needs to be updated
                if (!cityTemp.equals(foundInNewEntries.getMain().getTemp() + "")) {
                    // Batch an update for the existing record
                    Log.i(TAG, "Scheduling update: temp  " + cityServerID);
                    batch.add(ContentProviderOperation.newUpdate(City.CONTENT_URI)
                            .withSelection(City.COLUMN_ID + "='" + cityID + "'", null)
                            .withValue(City.COLUMN_TEMP, foundInNewEntries.getMain().getTemp() + "")
                            .build());
                    syncResult.stats.numUpdates++;
                }


            } else {

                // Entry doesn't exist, remove it from the local database
                Log.i(TAG, "Scheduling delete: city :" + cityServerID
                        + "  ID " + c.getString(c.getColumnIndex(City.COLUMN_NAME)));

                Uri deleteUri = City.CONTENT_URI.buildUpon()
                        .appendPath(c.getString(c.getColumnIndex(City.COLUMN_ID))).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());

                syncResult.stats.numDeletes++;

            }
            c.moveToNext();
        }
        c.close();

        // Add all the new entries
        for (com.task.dawadoz.forecast.rest.serverModels.innerModels.List awb : networkEntries.values()) {
            Log.i(TAG, "Scheduling insert: " + awb.getName() + awb.getId());

            batch.add(ContentProviderOperation.newInsert(City.CONTENT_URI)

                    .withValue(City.COLUMN_TEMP, awb.getMain().getTemp())
                    .withValue(City.COLUMN_SERVER_ID, awb.getId())
                    .withValue(City.COLUMN_NAME, awb.getName())

                    .build());

            syncResult.stats.numInserts++;


        }


        // Synchronize by performing batch update
        Log.i(TAG, "Merge solution ready, applying batch update...");


        try {


            resolver.applyBatch(SampleContentProvider.AUTHORITY, batch);
            resolver.notifyChange(City.CONTENT_URI, // URI where data was modified
                    null, // No local observer
                    false); // IMPORTANT: Do not sync to network


        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }


    private void syncNewsForecast(final SyncResult syncResult,String cityServerID)
            throws IOException, JSONException, RemoteException, OperationApplicationException {


//        apiService.getDataForSeveralDays().enqueue(new Callback<BulkDaysResponse>() {
        apiService.getDataForSeveralDays(cityServerID).enqueue(new Callback<BulkDaysResponse>() {
            @Override
            public void onResponse(Call<BulkDaysResponse> call, Response<BulkDaysResponse> response) {
                if (response.code() == 200) {

                    BulkDaysResponse networkEntries = response.body();


                    updateLocalDataBaseWithNewForecast(networkEntries, syncResult);


                } else {
                    Log.i(TAG, "response code =" + response.code());

                }


            }

            @Override
            public void onFailure(Call<BulkDaysResponse> call, Throwable t) {
                Log.i(TAG, "response :" + t.getMessage());

            }
        });

    }


    private void updateLocalDataBaseWithNewForecast(BulkDaysResponse networkEntries, SyncResult syncResult) {


        // Create list for batching ContentProvider transactions
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();

//        syncResult.stats.numEntries++;

        resolver.delete(ForecastData.CONTENT_URI, null, null);

        String cityID = String.valueOf(networkEntries.getCity().getId());

        // Add all the new entries
        for (List awb : networkEntries.getList()) {
            Log.i(TAG, "Scheduling insert: " + awb.getDtTxt() + cityID);

            syncResult.stats.numEntries++;

            String dtTxt, date ,hour;
            dtTxt = awb.getDtTxt();
            date = dtTxt.substring(0,dtTxt.indexOf(" "));
            hour = dtTxt.substring(dtTxt.indexOf(" ")).trim();



            batch.add(ContentProviderOperation.newInsert(ForecastData.CONTENT_URI)


                    .withValue(ForecastData.COLUMN_PARENT_CITY_ID, cityID)
                    .withValue(ForecastData.COLUMN_DATE, date)
                    .withValue(ForecastData.COLUMN_HOUR, hour)

                    .withValue(ForecastData.COLUMN_TEMP, awb.getMain().getTemp())
                    .withValue(ForecastData.COLUMN_WIND_SPEED, awb.getWind().getSpeed())
                    .withValue(ForecastData.COLUMN_PRESSURE, awb.getMain().getPressure())

                    .build());

            syncResult.stats.numInserts++;


        }


        // Synchronize by performing batch update
        Log.i(TAG, "Merge solution ready, applying batch update...");


        try {


            resolver.applyBatch(SampleContentProvider.AUTHORITY, batch);
            resolver.notifyChange(ForecastData.CONTENT_URI, // URI where data was modified
                    null, // No local observer
                    false); // IMPORTANT: Do not sync to network


        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }
}

