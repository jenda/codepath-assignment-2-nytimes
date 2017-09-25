package com.codepath.nytimesseach.settings;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jan_spidlen on 9/23/17.
 */

@Singleton
public class FilterSettings implements Serializable {

    public static SortOrder[] getAllOrderings() {
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
        LIVING("Living"),
        FOREIGN("Foreign");

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

    @Inject
    public FilterSettings() {
       for(int i = 0; i <  NewsDesk.values().length; i++) {
           final NewsDesk newsDesk = NewsDesk.values()[i];
           if (newsDesk !=  NewsDesk.None) {
               allowedNewsDesks.add(newsDesk);
           }
       }

        selectedNewsDesks = new HashSet<>();
        sortOrder = SortOrder.NewestFirst;
        beginDate = null;
    }

    public List<NewsDesk> getAllowedNewsDesks() {
        return this.allowedNewsDesks;
    }

    public SortOrder sortOrder;

    private Date beginDate;

    public void setBeginDate(Date date) {
        this.beginDate = date;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    private Set<NewsDesk> selectedNewsDesks;

    public boolean isSelected(NewsDesk newsDesk) {
        return selectedNewsDesks.contains(newsDesk);
    }

    public boolean select(NewsDesk newsDesk) {
        return selectedNewsDesks.add(newsDesk);
    }

    public boolean deselect(NewsDesk newsDesk) {
        return selectedNewsDesks.remove(newsDesk);
    }

    public boolean hasSelectedNewsDesks() {
        return !selectedNewsDesks.isEmpty();
    }

    public Set<NewsDesk> getSelectedNewsDesks() {
        return selectedNewsDesks;
    }

//    public static FilterSettings INSTANCE = new FilterSettings();

    public static FilterSettings getEmptyFilters() {
        return new FilterSettings();
    }
}
