package com.codepath.nytimesseach.fragments;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.codepath.nytimesseach.R;
import com.codepath.nytimesseach.controllers.FiltersController;
import com.codepath.nytimesseach.settings.FilterSettings;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class FilterFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.orderPicker)
    Spinner spinner;

    ArrayAdapter orderAdapter;
    FiltersController filtersController;
    FilterSettings filterSettings = FilterSettings.getEmptyFilters();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_filters_layout,
                container, false);

        ButterKnife.bind(this, view);

        filtersController = new FiltersController(this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(filtersController.getAdapter());

        filtersController.setData(filterSettings.getEmptyFilters());

        orderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
                filterSettings.getAllOrderings());
        orderAdapter.notifyDataSetChanged();
        spinner.setAdapter(orderAdapter);
        return view;
    }

    public static Fragment newInstance() {
        return new FilterFragment();
    }
}