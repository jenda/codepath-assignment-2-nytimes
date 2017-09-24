package com.codepath.nytimesseach.controllers;

import android.content.Context;

import com.airbnb.epoxy.Typed2EpoxyController;
import com.airbnb.epoxy.TypedEpoxyController;
import com.codepath.nytimesseach.settings.FilterSettings;
import com.codepath.nytimesseach.viewmodels.EpoxyCheckBoxModel_;
//import com.codepath.nytimesseach.viewmodels.EpoxyCheckBoxModel_;
//import com.codepath.nytimesseach.viewmodels.EpoxyCheckBox;
//import com.codepath.nytimesseach.viewmodels.EpoxyCheckBoxModel_;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class FiltersController extends TypedEpoxyController<FilterSettings> {

    private final Context context;

    public FiltersController(Context context) {
        this.context = context;
    }

    @Override
    protected void buildModels(FilterSettings data) {
        for(FilterSettings.NewsDesk newsDesk: data.getAllowedNewsDesks()) {
            String name = newsDesk.toString();
            new EpoxyCheckBoxModel_()
                    .id(name)
                    .text(name)
                    .addTo(this);
        }
    }
}
