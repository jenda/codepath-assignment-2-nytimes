package com.codepath.nytimesseach.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by jan_spidlen on 9/22/17.
 */

@Parcel
public class Headline {

    String main;

    @SerializedName("print_headline")
    String printHeadline;

    public String getMain() {
        return main;
    }

    public String getPrintHeadline() {
        return printHeadline;
    }
}
