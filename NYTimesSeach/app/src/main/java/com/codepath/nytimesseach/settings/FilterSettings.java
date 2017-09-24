package com.codepath.nytimesseach.settings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.name;
import static android.R.attr.value;

/**
 * Created by jan_spidlen on 9/23/17.
 */

public class FilterSettings {

    public SortOrder[] getAllOrderings() {
        return SortOrder.values();
    }

    public static enum SortOrder {
        OldestFirst("Oldest first"),
        NewestFirst("Newest first");

        private final String value;

        SortOrder(String value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }

    public static enum NewsDesk {
        None("None"),
        ARTS("Arts"),
        SPORTS("Sports"),
        SMARTER_LIVING("Smarter Living");

        private final String value;
        private NewsDesk(String value) {
            this.value = value;
        }
        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return value.equals(otherName);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private List<NewsDesk> allowedNewsDesks = new ArrayList<>();
    public FilterSettings() {
       for(int i = 0; i <  NewsDesk.values().length; i++) {
           final NewsDesk newsDesk = NewsDesk.values()[i];
           if (newsDesk !=  NewsDesk.None) {
               allowedNewsDesks.add(newsDesk);
           }
       }
    }

    public List<NewsDesk> getAllowedNewsDesks() {
        return this.allowedNewsDesks;
    }

    public SortOrder sortOrder;

    public Date beginDate;

    public static FilterSettings INSTANCE = new FilterSettings();



    public static FilterSettings getEmptyFilters() {
        return new FilterSettings();
    }
}
