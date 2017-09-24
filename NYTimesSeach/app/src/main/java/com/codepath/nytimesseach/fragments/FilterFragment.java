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
import android.widget.Button;
import android.widget.Spinner;

import com.codepath.nytimesseach.R;
import com.codepath.nytimesseach.controllers.FiltersController;
import com.codepath.nytimesseach.settings.FilterSettings;
import com.codepath.nytimesseach.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class FilterFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.orderPicker)
    Spinner spinner;

    @BindView(R.id.saveButton)
    Button saveButton;

    ArrayAdapter orderAdapter;
    FiltersController filtersController;
    FilterSettings filterSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_filters_layout,
                container, false);

        ButterKnife.bind(this, view);

        filterSettings = (FilterSettings)this.getArguments().getSerializable(Constants.FILTERS);

        filtersController = new FiltersController(this.getContext(), filterSettings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(filtersController.getAdapter());

        filtersController.setData(filterSettings.getEmptyFilters());

        orderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
                filterSettings.getAllOrderings());
        orderAdapter.notifyDataSetChanged();
        spinner.setAdapter(orderAdapter);

        spinner.setSelection(filterSettings.sortOrder.ordinal());
        spinner.setOnItemSelectedListener(filtersController);
        return view;
    }

    public static Fragment newInstance(FilterSettings filterSettings) {
        FilterFragment filterFragment = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.FILTERS, filterSettings);
        filterFragment.setArguments(bundle);
        return filterFragment;
    }

    @OnClick(R.id.saveButton)
    protected void onSave() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
