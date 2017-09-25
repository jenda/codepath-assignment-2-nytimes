package com.codepath.nytimesseach.fragments;

//import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.nytimesseach.R;
import com.codepath.nytimesseach.controllers.FiltersController;
import com.codepath.nytimesseach.data.DataFetcher;
import com.codepath.nytimesseach.settings.FilterSettings;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class FilterFragment extends BaseFragment {

    public static Fragment newInstance() {
        FilterFragment filterFragment = new FilterFragment();
        Bundle bundle = new Bundle();
        filterFragment.setArguments(bundle);
        return filterFragment;
    }
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.orderPicker)
    Spinner orderPicker;

    @BindView(R.id.saveButton)
    Button saveButton;

    @BindView(R.id.datePickerLayout)
    RelativeLayout datePickerLayout;

    @BindView(R.id.datePickerTextView)
    TextView datePickerTextView;

    @BindView(R.id.datePicker)
    DatePicker datePicker;

    ArrayAdapter orderAdapter;
    FiltersController filtersController;

    @Inject
    FilterSettings filterSettings;
    @Inject
    DataFetcher dataFetcher;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_filters_layout,
                container, false);

        ButterKnife.bind(this, view);

        getComponent().inject(this);

        filtersController = new FiltersController(this.getContext(), filterSettings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(filtersController.getAdapter());

        filtersController.setData(filterSettings.getEmptyFilters());

        orderAdapter = new ArrayAdapter(getContext(), R.layout.order_picker_item,
                filterSettings.getAllOrderings());
        orderAdapter.notifyDataSetChanged();
        orderPicker.setAdapter(orderAdapter);

        orderPicker.setSelection(filterSettings.getSortOrder().ordinal());
        orderPicker.setOnItemSelectedListener(filtersController);

        maybeUpdateDateLabel();
        return view;
    }

    private void maybeUpdateDateLabel() {
        if (filterSettings.getBeginDate() != null) {
            datePickerTextView.setText(simpleDateFormat.format(filterSettings.getBeginDate()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.datePickerTextView)
    protected void showDatePicker() {
        saveButton.setVisibility(View.GONE);
        datePickerLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.saveDateButton)
    protected void hideDatePickerAndSave() {
        Date newBeginDate = new Date(datePicker.getYear() - 1900,
                datePicker.getMonth(), datePicker.getDayOfMonth());
        filterSettings.setBeginDate(newBeginDate);
        maybeUpdateDateLabel();
        hideDatePicker();
    }

    @OnClick(R.id.cancelDateButton)
    protected void hideDatePicker() {
        saveButton.setVisibility(View.VISIBLE);
        datePickerLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.saveButton)
    protected void onSave() {
        getActivity().getSupportFragmentManager().popBackStack();
        onFiltersSaved();
    }

    private void onFiltersSaved() {
        dataFetcher.fetchFresh(filterSettings);
    }
}
