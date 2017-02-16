package com.horirevens.antarankantorpos;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.horirevens.antarankantorpos.antaran.AntaranPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LaunchActivity extends AppCompatActivity  {
    public static final String MY_LOG = "log_LaunchActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvAnippos, tvImei, tvAnama, tvNetwork;
    private String imeiNumber, anippos, anama, networkStatus;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private AlertDialog ade;

    private IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MY_LOG, "onReceive");
            networkStatus = NetworkStatus.getConnectivityStatusString(context.getApplicationContext());

            if (networkStatus == "1") {
                Log.i(MY_LOG, "onReceive 1");
                tvNetwork.setVisibility(View.GONE);
                tvAnama.setVisibility(View.VISIBLE);
                tvAnippos.setVisibility(View.VISIBLE);
            }
            if (networkStatus == "0") {
                Log.i(MY_LOG, "onReceive 0");
                tvNetwork.setVisibility(View.VISIBLE);
                tvAnama.setVisibility(View.GONE);
                tvAnippos.setVisibility(View.GONE);

                Snackbar.make(coordinatorLayout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Log.i(MY_LOG, "onCreate");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvAnama = (TextView) findViewById(R.id.tvAnama);
        tvAnippos = (TextView) findViewById(R.id.tvAnippos);
        tvNetwork = (TextView) findViewById(R.id.tvNetwork);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        tvAnama.setVisibility(View.VISIBLE);
        tvAnippos.setVisibility(View.VISIBLE);

        getImeiNumber();
        getOneAdruser();
    }

    private String getImeiNumber() {
        Log.i(MY_LOG, "getImeiNumber");
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imeiNumber = telephonyManager.getDeviceId();
        return imeiNumber;
    }

    private String getNippos() {
        String s = tvAnippos.getText().toString();
        Log.i(MY_LOG, "getNippos " + s);
        return s;
    }

    private void showSnackbar(String s) {
        snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("ulangi", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOneAdruser();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
        snackbar.show();
    }

    private void alertDialogIMEI(String response) {
        Log.i(MY_LOG, "alertDialogIMEI");
        if (response.equals("null")) {
            Log.i(MY_LOG, "alertDialogIMEI null");
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.alert_dialog_imei, null);

            tvImei = (TextView) view.findViewById(R.id.tvImei);
            tvImei.setText(imeiNumber);

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(MY_LOG, "alertDialogIMEI positive");
                    finish();
                }
            });
            adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Log.i(MY_LOG, "alertDialogIMEI dismiss");
                    finish();
                }
            });
            adb.setView(view);
            ade = adb.create();
            ade.show();
        } else {
            Log.i(MY_LOG, "alertDialogIMEI notNull");
            showOneAdruser(response);
            setupTabIcons();
            setupViewPager();
        }
    }

    private void getOneAdruser() {
        Log.i(MY_LOG, "getOneAdruser");
        String params = "?device_imei=" + imeiNumber;
        StringRequest stringRequest = new StringRequest(DBConfig.JSON_URL_LOGIN + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MY_LOG, "onResponse");
                        alertDialogIMEI(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MY_LOG, "onErrorResponse");
                        String s = "Gagal memuat data";
                        showSnackbar(s);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showOneAdruser(String json) {
        try {
            Log.i(MY_LOG, "parseJSON try");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(DBConfig.TAG_JSON_ARRAY);
            JSONObject jo = jsonArray.getJSONObject(0);

            anippos = jo.getString(DBConfig.TAG_ANIPPOS);
            anama = jo.getString(DBConfig.TAG_ANAMA);

            tvAnippos.setText(anippos);
            tvAnama.setText(anama);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupTabIcons() {
        Log.i(MY_LOG, "setupTabIcons");
        //AntaranTab1 antaranTab1 = (AntaranTab1) getSupportFragmentManager().getFragments();

        tabLayout.addTab(tabLayout.newTab().setText("Delivery Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Berhasil"));
        tabLayout.addTab(tabLayout.newTab().setText("Gagal"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setupViewPager() {
        Log.i(MY_LOG, "setupViewPager");
        final AntaranPagerAdapter antaranPagerAdapter = new AntaranPagerAdapter(
                getSupportFragmentManager(), tabLayout.getTabCount(), getNippos()
        );

        viewPager.setOffscreenPageLimit(6);
        viewPager.setAdapter(antaranPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        Log.i(MY_LOG, "onStart");
        super.onStart();
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        Log.i(MY_LOG, "onStop");
        super.onStop();
        unregisterReceiver(myReceiver);
        finish();
    }
}
