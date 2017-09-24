package com.codepath.nytimesseach.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.nytimesseach.R;
import com.codepath.nytimesseach.model.Document;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jan_spidlen on 9/24/17.
 */
public class WebViewArticleFragment extends Fragment {

    private static final String DOC_ID = "doc_id";
    private Document document;

    @BindView(R.id.webview)
    protected WebView webview;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_web_view_article,
                container, false);
        ButterKnife.bind(this, view);
        document = (Document)this.getArguments().getSerializable(DOC_ID);

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        webview.setWebViewClient(new OverridingWebViewClient());

        // Zooming and layout responsiveness.
//        webview.getSettings().setUseWideViewPort(true);
//        webview.getSettings().setLoadWithOverviewMode(true);
//        webview.getSettings().setSupportZoom(true);
//        webview.getSettings().setBuiltInZoomControls(true);
//        webview.getSettings().setDisplayZoomControls(false);

        Log.d("jenda", "weburl: " + document.getWebUrl());
        // Load the initial URL
        webview.loadUrl(document.getWebUrl());
        return view;
    }

    public static WebViewArticleFragment newInstance(Document document) {
        WebViewArticleFragment webViewArticleFragment = new WebViewArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DOC_ID, document);
        webViewArticleFragment.setArguments(bundle);
        return webViewArticleFragment;
    }

    private class OverridingWebViewClient extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}