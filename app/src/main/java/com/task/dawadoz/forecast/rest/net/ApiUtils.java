package com.task.dawadoz.forecast.rest.net;


public class ApiUtils {



    public static final String BASE_URL = "http://api.openweathermap.org";
    public static final String API_KEY = "&APPID=c3112b1593dae77777dde5625707e031";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
