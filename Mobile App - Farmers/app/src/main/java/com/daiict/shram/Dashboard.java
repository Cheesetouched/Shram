package com.daiict.shram;

import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daiict.shram.Adapters.ViewPagerAdapter;
import com.daiict.shram.Fragments.Market;
import com.daiict.shram.Fragments.Search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Dashboard extends AppCompatActivity {

    private ViewPager viewpager;

    private TabLayout tablayout;

    private CoordinatorLayout view;

    private AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        view = (CoordinatorLayout) findViewById(R.id.view);

        anim = (AnimationDrawable) view.getBackground();

        viewpager = (ViewPager) findViewById(R.id.viewpager);

        setUpViewPager(viewpager);

        tablayout = (TabLayout) findViewById(R.id.tablayout);

        tablayout.setupWithViewPager(viewpager);

        TabLayout.Tab tab = tablayout.getTabAt(0);

        tab.select();

        startAnim();

    }


    public void startAnim() {

        anim.setEnterFadeDuration(5000);

        anim.setExitFadeDuration(5000);

        anim.start();

    }


    public void setUpViewPager(ViewPager set) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Search(), "Search");

        adapter.addFragment(new Market(), "Market");

        set.setAdapter(adapter);

    }


}
