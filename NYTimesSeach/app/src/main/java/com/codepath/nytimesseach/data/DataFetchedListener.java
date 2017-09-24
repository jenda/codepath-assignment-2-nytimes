package com.codepath.nytimesseach.data;

import android.support.annotation.StringRes;

import com.codepath.nytimesseach.model.Document;

import java.util.List;

/**
 * Created by jan_spidlen on 9/24/17.
 */

public interface DataFetchedListener {
    public void onDataFetched(List<Document> docs);

    public void onErrorOccurred(@StringRes int messageRes);
}
