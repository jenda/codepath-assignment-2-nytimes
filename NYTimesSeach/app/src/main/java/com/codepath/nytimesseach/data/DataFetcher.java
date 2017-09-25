package com.codepath.nytimesseach.data;

import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.nytimesseach.R;
import com.codepath.nytimesseach.model.Document;
import com.codepath.nytimesseach.model.Response;
import com.codepath.nytimesseach.settings.FilterSettings;
import com.codepath.nytimesseach.utils.Utils;
import com.google.common.collect.ImmutableSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jan_spidlen on 9/23/17.
 */

@Singleton
public class DataFetcher {

    class RetryCounter {
        int counter;
    }

    private static Set<String> BLACK_LISTED_IDS = ImmutableSet.of(
            "56e0ea3e38f0d80718d563b2",
            "527ac73a38f0d86606634041");

    private static final String BASE_URL = "http://api.nytimes.com/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static NYTimesEndpointInterface apiService =
            retrofit.create(NYTimesEndpointInterface.class);

    private static String API_KEY = "8f987ade9ab543b782fdeb6dad48ada1";

    private DataFetchedListener dataFetchedListener;
    private final List<Document> dataDownloadedSoFar = new ArrayList<>();
    private final Set<String> idsDownloadedSoFar = new HashSet<>();
    private int currentPage = 0;
    private Call<Response> call = null;
    private boolean hasMore = true;
    private long lastAttempt = System.currentTimeMillis();
    private String query = null;

    @Inject
    public DataFetcher() {}

    private void clearBuffers() {
        hasMore = true;
        currentPage = 0;
        idsDownloadedSoFar.clear();
        dataDownloadedSoFar.clear();
    }

    private String buildFilterQuery(FilterSettings settings) {
        StringBuilder sb = new StringBuilder();
        if (settings.hasSelectedNewsDesks()) {
            sb.append("news_desk:(");
            for(FilterSettings.NewsDesk newsDesk: settings.getSelectedNewsDesks()) {
                sb.append("\"");
                sb.append(newsDesk.toString());
                sb.append("\" ");
            }
            sb.append(")");
        }

        String fg = sb.toString();
        if (fg == null || "".equals(fg)) {
            return null;
        }
        return fg;
    }


    public void setDataFetchedListener(DataFetchedListener dataFetchedListener) {
        this.dataFetchedListener = dataFetchedListener;
    }

    public synchronized void setQuery(String query) {
        this.query = query;
    }
    public synchronized void fetchMoreInitial() {
        fetchFresh(FilterSettings.getEmptyFilters());
    }


    public synchronized void fetchFresh(FilterSettings settings) {
        clearBuffers();
        fetchMore(0, settings);
    }

    public synchronized void fetchMore(FilterSettings settings) {
        if (call != null && !call.isExecuted() && !call.isCanceled()) {
            Log.d("jenda", "returning");
            return;
        }
        fetchMore(currentPage, settings);
    }

    @Nullable
    private String buildBeginDate(FilterSettings settings) {
        return settings.getBeginDate() == null ?
                null : new SimpleDateFormat("yyyyMMdd").format(settings.getBeginDate());
    }

    @Nullable
    private String buildFetchOrder(FilterSettings settings) {
        return settings.getSortOrder() == FilterSettings.SortOrder.OldestFirst ? "oldest" : null;
    }

    public synchronized void fetchMore(int page, FilterSettings settings) {
        if ("".equals(query)) {
            query = null;
        }
        if (!hasMore && lastAttempt > (System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(15))) {
            Log.d("jenda", "too soon");
            return;
        }

        // This should make sure that we have at most one request in progress.
        if (call != null && !call.isExecuted() && !call.isCanceled()) {
            Log.d("jenda", "Query in progress");
            return;
        }

        String filterQuery = buildFilterQuery(settings);
        String beginDate = buildBeginDate(settings);
        String fetchOrder = buildFetchOrder(settings);
        Log.d("jenda:", "page " +  page + "");
        Log.d("jenda:", "fg " +  filterQuery + "");
        Log.d("jenda:", "beginDate " +  beginDate + "");
        Log.d("jenda:", "query " +  query + "");

        final RetryCounter retryCounter = new RetryCounter();

        call = apiService.getDocs(query, page, fetchOrder, beginDate , API_KEY, filterQuery);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                if (response.code() == Utils.HTTP_TOO_MANY_REQUESTS) {
                    Log.d("jenda:", " retrying ...");
                    if (retryCounter.counter++ > 3) {
                        dataFetchedListener.onErrorOccurred(R.string.too_many_retries);
                        return;
                    }
                    try {
                        // TODO: make it exponential maybe.
                        Thread.currentThread().sleep(retryCounter.counter * 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    call.clone().enqueue(this);
                    return;
                } else if (response.code() != Utils.HTTP_OK) {
                    dataFetchedListener.onErrorOccurred(R.string.data_retrieval_failed);
                    return;
                }

                Response internalResponse = response.body();
                List<Document> docs = internalResponse.getResponse().getDocs();

                Log.d("jenda", "isSuccess: " + response.isSuccessful() + "");
                Log.d("jenda", "code: " + response.code() + "");
                Log.d("jenda", internalResponse.getStatus());
                Log.d("jenda", "size: " + internalResponse.getResponse().getDocs().size());
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
                dataFetchedListener.onErrorOccurred(R.string.network_error_message);
                t.printStackTrace();
            }
        });
    }
}
