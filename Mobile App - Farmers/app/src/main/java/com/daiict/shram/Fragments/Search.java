package com.daiict.shram.Fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daiict.shram.Dashboard;
import com.daiict.shram.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Search extends Fragment {

    private View view;

    private EditText search;

    private TextView header;

    private LinearLayout data;

    private String src = "";

    private StrictMode.ThreadPolicy policy;

    private ImageView display;

    private Str

    public Search() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search, container, false);

        display = (ImageView) view.findViewById(R.id.display);

        search = (EditText) view.findViewById(R.id.search);

        header = (TextView) view.findViewById(R.id.header);

        data = (LinearLayout) view.findViewById(R.id.data);

        setupSearch();

        return view;

    }


    public void setupSearch() {

        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    display.setVisibility(View.GONE);

                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    String searchterm = search.getText().toString();

                    getDisplayImage(searchterm);

                    return true;

                }

                return false;

            }

        });


    }


    private void alert(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }


    public void getDisplayImage(String image) {

        String webURL = "https://www.pexels.com/search/" + image;

        try {

            Document doc = Jsoup.connect(webURL).userAgent("Mozilla").get();

            Elements img = doc.select("img.photo-item__img");

            for (Element el : img) {

                src = el.absUrl("src");

                Picasso.with(getContext())
                        .load(src)
                        .fit()
                        .centerCrop()
                        .into(display);

                display.setVisibility(View.VISIBLE);

                break;

            }

            if (src == "") {

                alert("No Image Found!");

            }

            else {

                header.setVisibility(View.VISIBLE);

                header.setText("Results");

                header.setTextColor(getResources().getColor(R.color.black));

                data.setVisibility(View.VISIBLE);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}
