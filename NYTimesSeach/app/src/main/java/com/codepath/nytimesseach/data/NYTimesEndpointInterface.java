package com.codepath.nytimesseach.data;

import com.codepath.nytimesseach.model.Document;
import com.codepath.nytimesseach.model.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public interface NYTimesEndpointInterface {

    @GET("svc/search/v2/articlesearch.json")
    Call<Response> getDocs(@Query("q") String query,
                           @Query("page") int page,
                           @Query("api-key") String apiKey,
                           @Query("fq") String fq);
}
