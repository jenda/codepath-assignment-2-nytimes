package com.codepath.nytimesseach.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by jan_spidlen on 9/20/17.
 */
public class Response {
    private static Gson gson = new GsonBuilder().create();

    private String status;
    private String copyright;
    private InnerResponse response;

    public static Response fromJson(String serializedObj) {
        return gson.fromJson(serializedObj, Response.class);
    }

    public static Response fromJson(JSONObject obj) {
        return fromJson(obj.toString());
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
