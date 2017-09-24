package com.codepath.nytimesseach.data;

import android.util.Log;
import android.util.StringBuilderPrinter;

import com.codepath.nytimesseach.model.Document;
import com.codepath.nytimesseach.model.Response;
import com.codepath.nytimesseach.settings.FilterSettings;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class DataProvider {

    private static Set<String> BLACK_LISTED_IDS = ImmutableSet.of(
            "56e0ea3e38f0d80718d563b2",
            "527ac73a38f0d86606634041");

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
    private final List<Document> dataDownloadedSoFar = new ArrayList<>();
    private final Set<String> idsDownloadedSoFar = new HashSet<>();
    private int currentPage = 0;

    public DataProvider(DataFetchedListener dataFetchedListener) {
        this.dataFetchedListener = dataFetchedListener;
    }

    public void fetchMoreInitial() {
        fetchFresh(FilterSettings.getEmptyFilters());
    }


    public void fetchFresh(FilterSettings settings) {
        clearBuffers();
        fetchMore(0, settings);
    }

    private void clearBuffers() {
        currentPage = 0;
        idsDownloadedSoFar.clear();
        dataDownloadedSoFar.clear();
    }

    private String buildFG(FilterSettings settings) {
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
        return sb.toString();
    }

    public synchronized void fetchMore(FilterSettings settings) {
        if (call != null && !call.isExecuted() && !call.isCanceled()) {
            Log.d("jenda", "returning");
            return;
        }
        fetchMore(currentPage, settings);
    }
    Call<Response> call;
    public synchronized void fetchMore(int page, FilterSettings settings) {
        // TODO: finish data and sorting.

        if (call != null && !call.isExecuted() && !call.isCanceled()) {
            Log.d("jenda", "returning");
            return;
        }

        String fg = buildFG(settings);

        Log.d("jenda:", "page " +  page + "");
        Log.d("jenda:", "fg " +  fg + "");
//        Call<Response>
                call = apiService.getDocs("android", page, API_KEY, fg);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                Log.d("jenda", "isSuccess: " + response.isSuccessful() + "");
                Log.d("jenda", "code: " + response.code() + "");
                Response response1 = response.body();
                Log.d("jenda", response1.getStatus());
                Log.d("jenda", "size: " + response1.getResponse().getDocs().size());
                List<Document> docs = response1.getResponse().getDocs();
                for(Document doc: docs) {
                    if (idsDownloadedSoFar.add(doc.getId())) {
                        dataDownloadedSoFar.add(doc);
                    }
                }

                currentPage++;
                dataFetchedListener.onDataFetched(dataDownloadedSoFar);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
