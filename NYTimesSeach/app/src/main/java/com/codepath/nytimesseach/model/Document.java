package com.codepath.nytimesseach.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jan_spidlen on 9/20/17.
 */

@Parcel
public class Document implements Serializable {

    @SerializedName("web_url")
    String webUrl;

    String snippet;
    String source;
    List<Multimedia> multimedia;
    Headline headline;

    @SerializedName("pub_date")
    String pubDate;

    @SerializedName("document_type")
    String documentType;

    @SerializedName("new_desk")
    String newsDesk;

    @SerializedName("section_name")
    String sectionName;

    @SerializedName("type_of_material")
    String typeOfMaterial;

    @SerializedName("_id")
    String id;

    @SerializedName("word_count")
    int wordCount;

    double score;
    String uri;

    public String getWebUrl() {
        return webUrl;
    }

    public String getSnippet() {
        return snippet;
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


    public String getPubDate() {
        return pubDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public String getSectionName() {
        return sectionName;
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

        for (Multimedia multimedia : getMultimedia()) {
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
