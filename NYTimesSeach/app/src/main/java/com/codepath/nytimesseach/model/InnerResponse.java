package com.codepath.nytimesseach.model;

import java.util.List;

/**
 * Created by jan_spidlen on 9/22/17.
 */

public class InnerResponse {

    public static class Metadata {
        int hits;
        int offset;
        int time;
    }

    List<Document> docs;

    Metadata meta;
}
