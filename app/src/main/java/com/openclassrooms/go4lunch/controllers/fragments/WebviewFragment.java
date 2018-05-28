package com.openclassrooms.go4lunch.controllers.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.openclassrooms.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebviewFragment extends Fragment {

    private final String RESTAURANT_URL_KEY = "articleUrl";

    @BindView(R.id.fragment_webview_frame_layout) WebView myWebView;

    public WebviewFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        String articleUrl = args.getString(RESTAURANT_URL_KEY, "");

        myWebView.loadUrl(articleUrl);

        return view;
    }
}
