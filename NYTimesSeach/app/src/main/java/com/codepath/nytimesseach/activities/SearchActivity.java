package com.codepath.nytimesseach.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements
        DataFetchedListener, ItemClickSupport.OnItemClickListener {

    @BindView(R.id.resultsRecyclerView)
    RecyclerView resultsRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                DataProvider.INSTANCE.fetchMore("", FilterSettings.INSTANCE);
            }
        };

        resultsRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Document doc = documents.get(position);
        Log.d("jenda", "Opening doc: " + doc.getSnippet());
        transitionToModal(WebViewArticleFragment.newInstance(doc));
    }
}
