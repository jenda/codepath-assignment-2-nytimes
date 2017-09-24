package com.codepath.nytimesseach.fragments;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.nytimesseach.R;

import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class FilterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_filters_layout,
                container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    public static Fragment newInstance() {
        return new FilterFragment();
    }
}
