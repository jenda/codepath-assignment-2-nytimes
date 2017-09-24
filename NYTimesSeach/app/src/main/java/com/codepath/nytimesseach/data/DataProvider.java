package com.codepath.nytimesseach.data;

import android.util.Log;
import android.util.StringBuilderPrinter;

import com.codepath.nytimesseach.model.Document;
import com.codepath.nytimesseach.model.Response;
import com.codepath.nytimesseach.settings.FilterSettings;
import com.codepath.nytimesseach.utils.Constants;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    private Call<Response> call = null;
    private boolean hasMore = true;
    private long lastAttempt = System.currentTimeMillis();

    public DataProvider(DataFetchedListener dataFetchedListener) {
        this.dataFetchedListener = dataFetchedListener;
    }

    private void clearBuffers() {
        hasMore = true;
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

        String fg = sb.toString();
        if (fg == null || "".equals(fg)) {
            return null;
        }
        return fg;
    }

    public synchronized void fetchMoreInitial() {
        fetchFresh(FilterSettings.getEmptyFilters());
    }


    public synchronized void fetchFresh(FilterSettings settings) {
        clearBuffers();
        fetchMore(0, null, settings);
    }

    public synchronized void fetchMore(String query, FilterSettings settings) {
        if (call != null && !call.isExecuted() && !call.isCanceled()) {
            Log.d("jenda", "returning");
            return;
        }
        fetchMore(currentPage, query, settings);
    }
    class Counter {
        int counter;
    }
    public synchronized void fetchMore(int page, String query, FilterSettings settings) {
        // TODO: finish data and sorting.

        if (!hasMore && lastAttempt > (System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(15))) {
            Log.d("jenda", "too soon");
            return;
        }

        if (call != null && !call.isExecuted() && !call.isCanceled()) {
            Log.d("jenda", "returning");
            return;
        }

        String fg = buildFG(settings);

        Log.d("jenda:", "page " +  page + "");
        Log.d("jenda:", "fg " +  fg + "");

        final Counter retryCounter = new Counter();

        call = apiService.getDocs(query, page, API_KEY, fg);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                if (response.code() == Constants.HTTP_TOO_MANY_REQUESTS) {
                    Log.d("jenda:", " retrying ...");
                    if (retryCounter.counter++ > 3) {
                        throw new RuntimeException("too many retries");
                    }
                    try {
                        Thread.currentThread().sleep(retryCounter.counter * 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    call.clone().enqueue(this);
                    return;
                }

                Log.d("jenda", "isSuccess: " + response.isSuccessful() + "");
                Log.d("jenda", "code: " + response.code() + "");
                Response response1 = response.body();
                Log.d("jenda", response1.getStatus());
                Log.d("jenda", "size: " + response1.getResponse().getDocs().size());
                List<Document> docs = response1.getResponse().getDocs();

                // Dedup and filter out known bad articles.
                for(Document doc: docs) {
                    if (!BLACK_LISTED_IDS.contains(doc.getId())
                            && idsDownloadedSoFar.add(doc.getId())) {
                        dataDownloadedSoFar.add(doc);
                    }
                }

                Log.d("jenda", "dataDownloadedSoFar  " + dataDownloadedSoFar.size());

                lastAttempt = System.currentTimeMillis();
                hasMore = docs.size() == 10;
                if (hasMore) {
                    currentPage++;
                }
                dataFetchedListener.onDataFetched(dataDownloadedSoFar);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
