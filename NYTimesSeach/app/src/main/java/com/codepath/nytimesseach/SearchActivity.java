package com.codepath.nytimesseach;

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
import android.widget.Button;
import android.widget.EditText;

import com.codepath.nytimesseach.fragments.FilterFragment;
import com.codepath.nytimesseach.model.Document;
import com.codepath.nytimesseach.model.Response;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.searchButton)
    Button searchButton;

    @BindView(R.id.queryEditText)
    EditText searchEditText;

    @BindView(R.id.resultsRecyclerView)
    RecyclerView resultsRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArticlesAdapter articlesAdapter;
    private List<Document> documents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        searchButtonClicked();

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

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (id == R.id.action_filter) {
            Log.d("jenda", "filter button clicked");
            transitionToModal(FilterFragment.newInstance());
            return true;
        } else if (id == R.id.action_search) {
            Log.d("jenda", "search button clicked");
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.searchButton)
    public void searchButtonClicked() {
        String query = searchEditText.getText().toString();
        query = "android";
        Log.d("jenda", "query " + query);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "8f987ade9ab543b782fdeb6dad48ada1");
        params.put("page", 0);
        params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Response resp = Response.fromJson(response);
                Log.d("jenda", response.toString());
                Log.d("jenda", "resp: " + resp.toString());

                documents.clear();
                documents.addAll(resp.getResponse().getDocs());
                articlesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("");
                throwable.printStackTrace();
            }

        });
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
}
