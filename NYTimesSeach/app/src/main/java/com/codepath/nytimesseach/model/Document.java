package com.codepath.nytimesseach.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jan_spidlen on 9/20/17.
 */

public class Document {

    @SerializedName("web_url")
    private
    String webUrl;

    private String snippet;
    private Object blog;
    private String source;
    private List<Multimedia> multimedia;
    private Headline headline;
    private List<Object> keywords;

    @SerializedName("pub_date")
    private
    String pubDate;

    @SerializedName("document_type")
    private
    String documentType;

    @SerializedName("new_desk")
    private
    String newDesk;

    @SerializedName("section_name")
    private
    String sectionName;

    private Object byline;

    @SerializedName("type_of_material")
    private
    String typeOfMaterial;

    @SerializedName("_id")
    private
    String id;

    @SerializedName("word_count")
    private
    int wordCount;

    private double score;
    private String uri;

    public String getWebUrl() {
        return webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public Object getBlog() {
        return blog;
    }

    public String getSource() {
        return source;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }

    public List<Object> getKeywords() {
        return keywords;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getNewDesk() {
        return newDesk;
    }

    public String getSectionName() {
        return sectionName;
    }

    public Object getByline() {
        return byline;
    }

    public String getTypeOfMaterial() {
        return typeOfMaterial;
    }

    public String getId() {
        return id;
    }

    public int getWordCount() {
        return wordCount;
    }

    public double getScore() {
        return score;
    }

    public String getUri() {
        return uri;
    }

    public String getThumbnailOrImage() {
        if (getMultimedia() == null) {
            return null;
        }

        final Map<String, String> subTypesToUrls = new HashMap<>();

        for(Multimedia multimedia: getMultimedia()) {
            if (multimedia.getType().equals("image")) {
                subTypesToUrls.put(multimedia.getSubtype(), multimedia.getUrl());
            }
        }

        String url = null;
        if (subTypesToUrls.containsKey("xlarge")) {
            url = subTypesToUrls.get("xlarge");
        } else if (subTypesToUrls.containsKey("wide")) {
            url = subTypesToUrls.get("wide");
        } else if (subTypesToUrls.containsKey("thumbnail")) {
            url = subTypesToUrls.get("thumbnail");
        }

        if (url != null) {
            return "https://www.nytimes.com/" + url;
        }

        return null;
    }
}
