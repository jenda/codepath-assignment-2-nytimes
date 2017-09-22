package com.codepath.nytimesseach.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jan_spidlen on 9/20/17.
 */

public class Document {

    @SerializedName("web_url")
    String webUrl;

    String snippet;
    Object blog;
    String source;
    List<Object> multimedia;
    Object headline;
    List<Object> keywords;

    @SerializedName("pub_date")
    String pubDate;

    @SerializedName("document_type")
    String documentType;

    @SerializedName("new_desk")
    String newDesk;

    @SerializedName("section_name")
    String sectionName;

    Object byline;

    @SerializedName("type_of_material")
    String typeOfMaterial;

    @SerializedName("_id")
    String id;

    @SerializedName("word_count")
    int wordCount;

    double score;
    String uri;

}
