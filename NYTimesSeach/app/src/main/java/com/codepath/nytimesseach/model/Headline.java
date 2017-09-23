package com.codepath.nytimesseach.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jan_spidlen on 9/22/17.
 */

public class Headline {

    private String main;

    @SerializedName("print_headline")
    private
    String printHeadline;

    public String getMain() {
        return main;
    }

    public String getPrintHeadline() {
        return printHeadline;
    }
}
