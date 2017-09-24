package com.codepath.nytimesseach.data;

import android.util.Log;
import android.util.StringBuilderPrinter;

import com.codepath.nytimesseach.model.Document;
import com.codepath.nytimesseach.model.Response;
import com.codepath.nytimesseach.settings.FilterSettings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class DataProvider {


    public static interface DataFetchedListener {
        public void onDataFetched(List<Document> docs);
    }

    public static final String BASE_URL = "http://api.nytimes.com/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static NYTimesEndpointInterface apiService =
            retrofit.create(NYTimesEndpointInterface.class);

    private static String API_KEY = "8f987ade9ab543b782fdeb6dad48ada1";

    public static DataProvider INSTANCE;//= new DataProvider(dataFetchedListener);

    private final DataFetchedListener dataFetchedListener;

    public DataProvider(DataFetchedListener dataFetchedListener) {
        this.dataFetchedListener = dataFetchedListener;
    }

    public List<Document> fetchMoreInitial() {
        return fetchMore(0, FilterSettings.getEmptyFilters());
    }


    public List<Document> fetchNew(FilterSettings settings) {
        return fetchMore(0, settings);
    }

    public List<Document> fetchMore(int page, FilterSettings settings) {
        StringBuilder sb = new StringBuilder();
        if (settings.hasSelectedNewsDesks()) {
            sb.append("news_desk:(");
            for(FilterSettings.NewsDesk newsDesk: settings.getSelectedNewsDesks()) {
                sb.append("\"");
                sb.append(newsDesk.toString());
                sb.append("\"");
            }
            sb.append(")");
        }


        // TODO: finish data and sorting.

        String fg = sb.toString();// "news_desk:(\"Sports\" \"Foreign\")";

        Log.d("jenda", fg);


        Call<Response> call = apiService.getDocs("android", page, API_KEY, fg);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                Response response1 = response.body();
                Log.d("jenda", response1.getStatus());
                Log.d("jenda", "size: " + response1.getResponse().getDocs().size());
                dataFetchedListener.onDataFetched(response1.getResponse().getDocs());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return null;
    }
}
