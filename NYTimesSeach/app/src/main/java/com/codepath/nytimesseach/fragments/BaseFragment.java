package com.codepath.nytimesseach.fragments;

import android.support.v4.app.Fragment;

import com.codepath.nytimesseach.MyApp;
import com.codepath.nytimesseach.dagger.NYTimesComponent;

/**
 * Created by jan_spidlen on 9/24/17.
 */

public abstract class BaseFragment extends Fragment {

    protected NYTimesComponent getComponent() {
        return ((MyApp)getActivity().getApplication()).getNetComponent();
    }
}
