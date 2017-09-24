package com.codepath.nytimesseach.settings;

import java.util.Date;

import static android.R.attr.name;
import static android.R.attr.value;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class FilterSettings {

    static enum SortOrder {
        OldestFirst,
        NewestFirst;
    };

    static enum NewsDesk {
        None("None"),
        ARTS("Arts"),
        SPORTS("Sports");

        private final String value;
        private NewsDesk(String value) {
            this.value = value;
        }
        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return value.equals(otherName);
        }

        public String toString() {
            return value;
        }
    };

    public SortOrder sortOrder;

    public Date beginDate;

    public static FilterSettings INSTANCE = new FilterSettings();

    public static FilterSettings getEmptyFilters() {
        return new FilterSettings();
    }
}
