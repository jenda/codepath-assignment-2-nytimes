package com.codepath.nytimesseach.data;

import android.util.Log;

import com.codepath.nytimesseach.model.Document;
import com.codepath.nytimesseach.model.Response;
import com.codepath.nytimesseach.settings.FilterSettings;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class DataProvider {
    public static final String BASE_URL = "http://api.nytimes.com/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static NYTimesEndpointInterface apiService =
            retrofit.create(NYTimesEndpointInterface.class);

    private static String API_KEY = "8f987ade9ab543b782fdeb6dad48ada1";

    public List<Document> fetchMoreInitial() {
        return fetchMore(0, FilterSettings.getEmptyFilters());
    }

    List<Document> fetchMore(int page, FilterSettings settings) {
        Call<Response> call = apiService.getDocs("android", page, API_KEY);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                Response response1 = response.body();
                Log.d("jenda", response1.getStatus());
                Log.d("jenda", "size: " + response1.getResponse().getDocs().size());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return null;
    }
}
