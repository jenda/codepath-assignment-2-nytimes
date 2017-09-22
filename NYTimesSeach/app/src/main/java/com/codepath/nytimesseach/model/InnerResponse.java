package com.codepath.nytimesseach.model;

import java.util.List;

/**
 * Created by jan_spidlen on 9/22/17.
 */

public class InnerResponse {

    public List<Document> getDocs() {
        return docs;
    }

    public InnerResponseMetadata getMeta() {
        return meta;
    }

    private List<Document> docs;

    private InnerResponseMetadata meta;
}
