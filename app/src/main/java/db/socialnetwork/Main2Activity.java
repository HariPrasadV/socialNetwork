package db.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;

import static db.socialnetwork.MainActivity.BaseURL;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        String uid = (getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).getString("id",null);
        //new getFollowedPosts().execute();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        SimpleFragmentPagerAdapter s = new SimpleFragmentPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(s);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}

