package com.codepath.nytimesseach.activities;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.codepath.nytimesseach.R;
import com.codepath.nytimesseach.adapters.ArticlesAdapter;
import com.codepath.nytimesseach.controllers.EndlessRecyclerViewScrollListener;
import com.codepath.nytimesseach.data.DataFetchedListener;
import com.codepath.nytimesseach.data.DataProvider;
import com.codepath.nytimesseach.fragments.WebViewArticleFragment;
import com.codepath.nytimesseach.fragments.FilterFragment;
import com.codepath.nytimesseach.model.Document;
import com.codepath.nytimesseach.settings.FilterSettings;
import com.codepath.nytimesseach.utils.ItemClickSupport;
import com.codepath.nytimesseach.utils.Utils;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.common.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.message;
import static android.support.design.widget.Snackbar.make;

public class SearchActivity extends AppCompatActivity implements
        DataFetchedListener, ItemClickSupport.OnItemClickListener {

    @BindView(R.id.resultsRecyclerView)
    RecyclerView resultsRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.outer_layout)
    LinearLayout outerLayout;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArticlesAdapter articlesAdapter;
    private List<Document> documents;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (!Utils.isNetworkAvailable(this) || !Utils.isOnline()) {
            displayErrorMessage(R.string.internet_is_not_accessible);
        }

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);

        documents = new ArrayList<>();
        articlesAdapter = new ArticlesAdapter(documents, getApplicationContext());

        resultsRecyclerView.setAdapter(articlesAdapter);
        resultsRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        articlesAdapter.notifyDataSetChanged();
        DataProvider.INSTANCE = new DataProvider(this);
        DataProvider.INSTANCE.fetchMoreInitial();

        ItemClickSupport.addTo(resultsRecyclerView).setOnItemClickListener(this);

        endlessRecyclerViewScrollListener =
                new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("jenda", "onLoadMore");
                DataProvider.INSTANCE.fetchMore(FilterSettings.INSTANCE);
            }
        };

        resultsRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DataProvider.INSTANCE.setQuery(query);
                DataProvider.INSTANCE.fetchFresh(FilterSettings.INSTANCE);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.equals("")) {
                    DataProvider.INSTANCE.setQuery(null);
                    DataProvider.INSTANCE.fetchFresh(FilterSettings.INSTANCE);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            Log.d("jenda", "filter button clicked");
            transitionToModal(FilterFragment.newInstance(FilterSettings.INSTANCE));
            return true;
        } else if (id == R.id.action_search) {
            Log.d("jenda", "search button clicked");
        }

        return super.onOptionsItemSelected(item);
    }

    private void transitionToModal(Fragment newFragment) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_bottom, R.anim.fragment_slide_delay,
                        0, R.anim.exit_bottom);

        fragmentTransaction
                .add(R.id.modal_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDataFetched(List<Document> docs) {
        documents.clear();
        documents.addAll(docs);
        articlesAdapter.notifyDataSetChanged();
        endlessRecyclerViewScrollListener.resetState();
    }

    @Override
    public void onErrorOccurred(@StringRes int messageRes) {
        displayErrorMessage(messageRes);
    }

    private void displayErrorMessage(@StringRes int messageRes) {
        final Snackbar snackbar = Snackbar.make(outerLayout, getApplicationContext().getText(messageRes),
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Document doc = documents.get(position);
        Log.d("jenda", "Opening doc: " + doc.getSnippet());
        transitionToModal(WebViewArticleFragment.newInstance(doc));
    }
}
