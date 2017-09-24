package com.codepath.nytimesseach.controllers;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.airbnb.epoxy.Typed2EpoxyController;
import com.airbnb.epoxy.TypedEpoxyController;
import com.codepath.nytimesseach.settings.FilterSettings;
import com.codepath.nytimesseach.viewmodels.EpoxyCheckBoxModel_;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class FiltersController extends TypedEpoxyController<FilterSettings>
        implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private final Context context;
    private final FilterSettings filterSettings;
    public FiltersController(Context context, FilterSettings filterSettings) {
        this.context = context;
        this.filterSettings = filterSettings;
    }

    @Override
    protected void buildModels(FilterSettings data) {
        for(FilterSettings.NewsDesk newsDesk: filterSettings.getAllowedNewsDesks()) {
            String name = newsDesk.toString();
            new EpoxyCheckBoxModel_()
                    .id(name)
                    .text(name)
                    .checked(filterSettings.isSelected(newsDesk))
                    .onCheckedChangeListener(this)
                    .tag(newsDesk)
                    .addTo(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filterSettings.sortOrder = FilterSettings.getAllOrderings()[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        FilterSettings.NewsDesk newsDesk = (FilterSettings.NewsDesk)buttonView.getTag();
        if (isChecked) {
            filterSettings.select(newsDesk);
        } else {
            filterSettings.deselect(newsDesk);
        }
    }
}
