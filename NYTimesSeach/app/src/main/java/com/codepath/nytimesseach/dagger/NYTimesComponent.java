package com.codepath.nytimesseach.dagger;

import com.codepath.nytimesseach.activities.SearchActivity;
import com.codepath.nytimesseach.fragments.FilterFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jan_spidlen on 9/24/17.
 */

@Singleton
@Component
public interface NYTimesComponent {
    void inject(SearchActivity activity);

    void inject(FilterFragment filterFragment);
}
