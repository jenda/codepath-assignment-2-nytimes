package com.codepath.nytimesseach.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by jan_spidlen on 9/20/17.
 */

public class Response {
    private static Gson gson = new GsonBuilder().create();

    private String status;
    private String copyright;
    private InnerResponse response;

    public static Response fromJson(JSONObject obj) {
        return gson.fromJson(obj.toString(), Response.class);
    }

    public String getStatus() {
        return status;
    }

    public String getCopyright() {
        return copyright;
    }

    public InnerResponse getResponse() {
        return response;
    }
}
