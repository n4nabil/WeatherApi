package com.task.dawadoz.forecast.views;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.task.dawadoz.forecast.R;
import com.task.dawadoz.forecast.localDataProvider.data.City;
import com.task.dawadoz.forecast.rest.syncControllers.SyncAdapter;

public class MyCitiesRecyclerViewAdapter extends RecyclerView.Adapter<MyCitiesRecyclerViewAdapter.ViewHolder> {

    Cursor mCursor = null;
    private Context context;

    public MyCitiesRecyclerViewAdapter(Context context/*, OnListFragmentInteractionListener listener*/) {
        this.context = context;
//        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cities, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        mCursor.moveToPosition(position);

//        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(City.COLUMN_NAME)));
        holder.mContentView.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(City.COLUMN_SERVER_ID)));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SyncAdapter.performSync(holder.mContentView.getText().toString());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        if (mCursor != null) {
            mCursor.moveToFirst();
            notifyDataSetChanged();
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
//        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

    }
}
