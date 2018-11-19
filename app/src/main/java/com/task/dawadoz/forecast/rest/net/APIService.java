package com.task.dawadoz.forecast.rest.net;


import com.task.dawadoz.forecast.rest.serverModels.BulkCitiesResponse;
import com.task.dawadoz.forecast.rest.serverModels.BulkDaysResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {


    @GET("/data/2.5/group?id=524901,703448,2643743,2172797,1851632&units=metric" + ApiUtils.API_KEY)
    Call<BulkCitiesResponse> getDataForSeveralCities();

    @GET("/data/2.5/forecast?" + ApiUtils.API_KEY)
    Call<BulkDaysResponse> getDataForSeveralDays(@Query("id") String cityServerID );


}
