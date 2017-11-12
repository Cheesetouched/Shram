package com.daiict.shram.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
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
import com.daiict.shram.R;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Search extends Fragment {

    private String USERNAME = "6a872cfb-9761-41ce-a308-271a80101b0a";

    private String PASSWORD = "VF1nPTAeKpNt";

    private String DARK_SKY_API = "http://sastimasti.me/shram/index.php?lat=23.186832&lon=72.628724&name=";

    private View view;

    private EditText search;

    private TextView header;

    private LinearLayout data;

    private String searchterm;

    private String src = "";

    private String dark_sky;

    private JSONObject dark_sky_object;

    private JSONArray dark_sky_array;

    private String is_temp;

    private String ns_temp;

    private String is_hum;

    private String ns_hum;

    private String is_ph;

    private String ns_ph;

    private String season;

    private String growspan;

    private String price_min;

    private String price_max;

    private float price;

    private StrictMode.ThreadPolicy policy;

    private ImageView display;

    private TextView i_temp;

    private TextView n_temp;

    private TextView i_hum;

    private TextView n_hum;

    private TextView i_ph;

    private TextView n_ph;

    private TextView theseason;

    private TextView theprice;

    private TextView thegrowspan;

    public Search() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search, container, false);

        display = (ImageView) view.findViewById(R.id.display);

        search = (EditText) view.findViewById(R.id.search);

        header = (TextView) view.findViewById(R.id.header);

        data = (LinearLayout) view.findViewById(R.id.data);

        i_temp = (TextView) view.findViewById(R.id.i_temp);

        n_temp = (TextView) view.findViewById(R.id.n_temp);

        i_hum = (TextView) view.findViewById(R.id.i_hum);

        n_hum = (TextView) view.findViewById(R.id.n_hum);

        i_ph = (TextView) view.findViewById(R.id.i_ph);

        n_ph = (TextView) view.findViewById(R.id.n_ph);

        theseason = (TextView) view.findViewById(R.id.theseason);

        theprice = (TextView) view.findViewById(R.id.theprice);

        thegrowspan = (TextView) view.findViewById(R.id.thegrowspan);

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

                    searchterm = search.getText().toString();

                    getImage(searchterm);

                    return true;

                }

                return false;

            }

        });


    }


    private void alert(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }


    public void getImage(final String image) {

        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(getContext(), R.style.MyTheme);

                loading.setCancelable(false);

                loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

                loading.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                Picasso.with(getContext())
                        .load(s)
                        .fit()
                        .centerCrop()
                        .into(display);

                display.setVisibility(View.VISIBLE);

                if (s == "") {

                    alert("No Image Found!");

                } else {

                    header.setVisibility(View.VISIBLE);

                    header.setText("Results");

                    header.setTextColor(getResources().getColor(R.color.black));

                    data.setVisibility(View.VISIBLE);

                }

            }

            @Override
            protected String doInBackground(String... params) {

                String result = getImg(image);

                return result;

            }
        }

        RegisterUser ru = new RegisterUser();

        ru.execute(image);

    }


    public String getImg(String image) {

        String webURL = "https://www.pexels.com/search/" + image;

        try {

            Document doc = Jsoup.connect(webURL).userAgent("Mozilla").get();

            Elements img = doc.select("img.photo-item__img");

            for (Element el : img) {

                src = el.absUrl("src");

                break;

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        return src;

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

            } else {

                header.setVisibility(View.VISIBLE);

                header.setText("Results");

                header.setTextColor(getResources().getColor(R.color.black));

                data.setVisibility(View.VISIBLE);

                getTemp(searchterm);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    private void getTemp(String vegetable) {

        try {

            Document doc = Jsoup.connect("https://www.google.com/search?q=" + vegetable + "+temperature").get();

            Elements temp = doc.select("span._Tgc");

            for (Element element : temp) {

                String t = element.text();

                NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, USERNAME, PASSWORD);

                EntitiesOptions entities = new EntitiesOptions.Builder()
                        .sentiment(true)
                        .limit(1)
                        .build();

                Features features = new Features.Builder()
                        .entities(entities)
                        .build();

                AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                        .text(t)
                        .features(features)
                        .build();

                AnalysisResults response = service
                        .analyze(parameters)
                        .execute();

                try {

                    JSONObject result = new JSONObject(response.toString());

                    JSONArray ent = result.getJSONArray("entities");

                    for (int i = 0; i < ent.length(); i++) {

                        JSONObject jsonobject = ent.getJSONObject(i);

                        is_temp = jsonobject.getString("text");

                    }

                    i_temp.setText(is_temp);


                    // Fetch JSON From DataSet

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpGet httpget = new HttpGet(DARK_SKY_API + searchterm);

                    HttpResponse res = httpclient.execute(httpget);

                    if (res.getStatusLine().getStatusCode() == 200) {


                        // Sort The JSON By Objects & Arrays

                        dark_sky = EntityUtils.toString(res.getEntity());

                        dark_sky_object = new JSONObject(dark_sky);

                        dark_sky_array = dark_sky_object.getJSONArray("data");


                        // Get Specific Data For Current Conditions From DataSet

                        for (int i = 0; i < dark_sky_array.length(); i++) {

                            JSONObject jsonobject = dark_sky_array.getJSONObject(i);

                            ns_temp = jsonobject.getString("Temperature High");

                            ns_hum = jsonobject.getString("Humidity");

                            ns_ph = jsonobject.getString("pH");

                            season = jsonobject.getString("Season");

                            growspan = jsonobject.getString("Growth Span");

                            price_min = jsonobject.getString("Price Min");

                            price_max = jsonobject.getString("Price Max");

                            float p_min = Integer.parseInt(price_min);

                            float p_max = Integer.parseInt(price_max);

                            // Set Market Price

                            price = (p_min + p_max) / 2;

                            theprice.setText("₹ " + String.valueOf(price) + "/KG");


                            // Set Grow Span

                            thegrowspan.setText(growspan + " Days Approx.");


                            // Set Season

                            theseason.setText(season);


                            // Set Current Temperature

                            float t_now = Float.parseFloat(ns_temp);

                            t_now = Math.round(t_now);

                            n_temp.setText(String.valueOf(t_now) + "°C");


                            // Set Current Humidity

                            float h_now = Float.parseFloat(ns_hum);

                            h_now = h_now * 100;

                            n_hum.setText(String.valueOf(h_now) + "%");

                            getHum(searchterm);


                            // Set pH

                            i_ph.setText(is_ph);

                            float now = Float.parseFloat(ns_ph);

                            n_ph.setText(String.valueOf(now));


                        }

                    } else {

                        alert("Failed To Get Current Weather Status!");

                    }

                } catch (JSONException je) {

                    alert(je.toString());

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    private void getHum(String vegetable) {

        try {

            Document doc = Jsoup.connect("https://www.google.com/search?q=" + vegetable + "+humidity").get();

            Elements temp = doc.select("span._Tgc");

            for (Element element : temp) {

                String t = element.text();

                NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, USERNAME, PASSWORD);

                EntitiesOptions entities = new EntitiesOptions.Builder()
                        .sentiment(true)
                        .limit(1)
                        .build();

                Features features = new Features.Builder()
                        .entities(entities)
                        .build();

                AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                        .text(t)
                        .features(features)
                        .build();

                AnalysisResults response = service
                        .analyze(parameters)
                        .execute();

                try {

                    JSONObject result = new JSONObject(response.toString());

                    JSONArray ent = result.getJSONArray("entities");

                    for (int i = 0; i < ent.length(); i++) {

                        JSONObject jsonobject = ent.getJSONObject(i);

                        is_hum = jsonobject.getString("text");

                    }

                    i_hum.setText(is_hum + " %");

                    getPH(searchterm);


                } catch (JSONException je) {

                    alert(je.toString());

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    private void getPH(String vegetable) {

        try {

            Document doc = Jsoup.connect("https://www.google.com/search?q=" + vegetable + "+ph").get();

            Elements temp = doc.select("span._Tgc");

            for (Element element : temp) {

                String t = element.text();

                NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, USERNAME, PASSWORD);

                EntitiesOptions entities = new EntitiesOptions.Builder()
                        .sentiment(true)
                        .limit(1)
                        .build();

                Features features = new Features.Builder()
                        .entities(entities)
                        .build();

                AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                        .text(t)
                        .features(features)
                        .build();

                AnalysisResults response = service
                        .analyze(parameters)
                        .execute();

                try {

                    JSONObject result = new JSONObject(response.toString());

                    JSONArray ent = result.getJSONArray("entities");

                    for (int i = 0; i < ent.length(); i++) {

                        JSONObject jsonobject = ent.getJSONObject(i);

                        is_ph = jsonobject.getString("text");

                    }

                } catch (JSONException je) {

                    alert(je.toString());

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}
