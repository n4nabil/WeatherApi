package com.task.dawadoz.forecast.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.task.dawadoz.forecast.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CitiesFragment.newInstance(1))
                    .commitNow();
        }


    }


//    APIService apiService = ApiUtils.getAPIService();
//        apiService.getDataForSeveralCities().enqueue(new Callback<BulkCitiesResponse>() {
//            @Override
//            public void onResponse(Call<BulkCitiesResponse> call, Response<BulkCitiesResponse> response) {
//                Toast.makeText(MainActivity.this, "code:" +response.code(), Toast.LENGTH_SHORT).show();
//
//                Log.d("AAA", "onTemp: "+response.body().getList().get(0).getMain().getTemp().toString());
//            }
//
//            @Override
//            public void onFailure(Call<BulkCitiesResponse> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "t:" +t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("AAA", "onFailure: "+t.getMessage());
//
//            }
//        });
//
//
//        apiService.getDataForSeveralDays().enqueue(new Callback<BulkCitiesResponse>() {
//            @Override
//            public void onResponse(Call<BulkCitiesResponse> call, Response<BulkCitiesResponse> response) {
//                Toast.makeText(MainActivity.this, "code:" +response.code(), Toast.LENGTH_SHORT).show();
//                Log.d("AAA", "onTemp: "+response.body().getList().get(0).getMain().getTemp().toString());
//
//            }
//
//            @Override
//            public void onFailure(Call<BulkCitiesResponse> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "t:" +t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("AAA", "onFailure: "+t.getMessage());
//            }
//        });



}
