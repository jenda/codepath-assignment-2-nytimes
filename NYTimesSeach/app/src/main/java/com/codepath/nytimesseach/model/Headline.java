package com.codepath.nytimesseach.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jan_spidlen on 9/22/17.
 */

public class Headline {

    String main;

    @SerializedName("print_headline")
    String printHeadline;
}
