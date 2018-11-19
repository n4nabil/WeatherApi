package com.task.dawadoz.forecast.views;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.task.dawadoz.forecast.R;
import com.task.dawadoz.forecast.localDataProvider.data.City;
import com.task.dawadoz.forecast.rest.syncControllers.AccountGeneral;
import com.task.dawadoz.forecast.rest.syncControllers.SyncAdapter;

public class CitiesFragment extends Fragment {

    //
    private static final String ARG_COLUMN_COUNT = "column-count";
    //
    private int mColumnCount = 1;

    MyCitiesRecyclerViewAdapter mAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CitiesFragment() {
    }


    @SuppressWarnings("unused")
    public static CitiesFragment newInstance(int columnCount) {
        CitiesFragment fragment = new CitiesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        mAdapter = new MyCitiesRecyclerViewAdapter(getContext());


        // Create your sync account
        AccountGeneral.createSyncAccount(getActivity());

        // Perform a manual sync by calling this:
        SyncAdapter.performSync();


        // Setup example content observer
        articleObserver = new ArticleObserver();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mAdapter);
        }


//        updateWithNewOrders();

        return view;
    }


    private ArticleObserver articleObserver;

    @Override
    public void onStart() {
        super.onStart();

        // Register the observer at the start of our activity
        getActivity().getContentResolver().registerContentObserver(
                City.CONTENT_URI, // Uri to observe (our articles)
                true, // Observe its descendants
                articleObserver); // The observer


    }

    @Override
    public void onResume() {
        super.onResume();
        refreshArticles();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (articleObserver != null) {
            // Unregister the observer at the stop of our activity
            getActivity().getContentResolver().unregisterContentObserver(articleObserver);
        }
    }


    private void getCursorFromDBNew() {
//        mAdapter.setAdapter(SampleDatabase.getInstance(getActivity()).orderDAO()
//                .selectAllNew(Order.STATUS_NEW));

        Cursor c = getContext().getContentResolver().query(City.CONTENT_URI,
                null, null, null, null, null);
        mAdapter.setCursor(c);

    }

    public void updateWithNewOrders() {

//        getActivity().getSupportLoaderManager().restartLoader(CURSOR_LOADER_New, null, mLoaderCallbacks);

        getCursorFromDBNew();
    }


    private static final int CURSOR_LOADER_New = 1;

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    switch (id) {
                        case CURSOR_LOADER_New:
                            Log.d("AAA", "onCreateLoader: " + "fired");
                            return new CursorLoader(getContext(),
                                    City.CONTENT_URI,
                                    null,
                                    null, null, null);
                        default:
                            throw new IllegalArgumentException();
                    }
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    switch (loader.getId()) {
                        case CURSOR_LOADER_New:
                            mAdapter.setCursor(data);
                            Log.d("AAA", "onLoadFinished: " + data.getCount());
                            break;
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    switch (loader.getId()) {
                        case CURSOR_LOADER_New:
                            mAdapter.setCursor(null);
                            break;
                    }
                }

            };

    /**
     * Example content observer for observing article data changes.
     */
    private final class ArticleObserver extends ContentObserver {
        private ArticleObserver() {
            // Ensure callbacks happen on the UI thread
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            // Handle your data changes here!!!
            refreshArticles();
        }
    }

    private void refreshArticles() {
        Toast.makeText(getActivity(), "Articles data has changed!", Toast.LENGTH_SHORT).show();
        updateWithNewOrders();
    }

}
