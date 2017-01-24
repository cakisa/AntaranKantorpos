package com.horirevens.antarankantorpos.about;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.horirevens.antarankantorpos.BuildConfig;
import com.horirevens.antarankantorpos.R;

public class AboutActivity extends AppCompatActivity {
    public static final String MY_LOG = "log_AboutActivity";

    private Toolbar toolbar;
    private TextView tvVersi;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AboutPagerAdapter aboutPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Log.i(MY_LOG, "onCreate");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tvVersi = (TextView) findViewById(R.id.tv_versi);
        String versionName = BuildConfig.VERSION_NAME;
        tvVersi.setText("Antaran Kantorpos \nversi " + versionName);

        setupTabsIcon();
        setupViewPager();
    }

    private void setupTabsIcon() {
        Log.i(MY_LOG, "setupTabIcons");
        tabLayout.addTab(tabLayout.newTab().setText("Manajer Antaran"));
        tabLayout.addTab(tabLayout.newTab().setText("Engine Antaran"));
        tabLayout.addTab(tabLayout.newTab().setText("Android Antaran"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setupViewPager() {
        Log.i(MY_LOG, "setupViewPager");
        aboutPagerAdapter = new AboutPagerAdapter(
                getSupportFragmentManager(), tabLayout.getTabCount()
        );
        Log.i(MY_LOG, "0");
        viewPager.setOffscreenPageLimit(3);
        Log.i(MY_LOG, "1");
        viewPager.setAdapter(aboutPagerAdapter);
        Log.i(MY_LOG, "2");
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Log.i(MY_LOG, "3");

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
