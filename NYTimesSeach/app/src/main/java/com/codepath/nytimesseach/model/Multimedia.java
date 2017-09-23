package com.codepath.nytimesseach.model;

/**
 * Created by jan_spidlen on 9/22/17.
 */

public class Multimedia {

    private String type;
    private String subtype; // xlarge, wide, thumbnail
    private String url;
    private int height;
    private int width;
    private int rank;
    private Object legacy;

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getUrl() {
        return url;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getRank() {
        return rank;
    }

    public Object getLegacy() {
        return legacy;
    }
}
