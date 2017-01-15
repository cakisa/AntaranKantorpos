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
import com.horirevens.antarankantorpos.libs.MyImei;
import com.horirevens.antarankantorpos.network.NetworkStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String MY_LOG = "log_MainActivity";

    public static String[] anippos;
    public static String[] anama;

    public TextView tvAnippos, tvAnama, tvNetwork;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private String imeiNumber, networkStatus;
    private TextView tvImei;
    private AlertDialog ade, adi;
    private ViewPager viewPager;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MY_LOG, "onReceive");
            networkStatus = NetworkStatus.getConnectivityStatusString(context.getApplicationContext());

            if (networkStatus == "1") {
                tvNetwork.setVisibility(View.GONE);
                tvAnama.setVisibility(View.VISIBLE);
                tvAnippos.setVisibility(View.VISIBLE);
            }
            if (networkStatus == "0") {
                tvNetwork.setVisibility(View.VISIBLE);
                tvAnama.setVisibility(View.GONE);
                tvAnippos.setVisibility(View.GONE);

                Snackbar.make(coordinatorLayout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MY_LOG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAnama = (TextView) findViewById(R.id.anama);
        tvAnippos = (TextView) findViewById(R.id.anippos);
        tvNetwork = (TextView) findViewById(R.id.tvNetwork);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        //nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        //nestedScrollView.setFillViewport(true);
        //nestedScrollView.setNestedScrollingEnabled(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Test");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        getImeiNumber();
        getOneAdruser();
    }

    private String getImeiNumber() {
        Log.i(MY_LOG, "getImeiNumber");
        MyImei myImei = new MyImei();
        imeiNumber = myImei.getImeiNumber(getApplicationContext());
        Log.i(MY_LOG, "getImeiNumber imeiNumber");
        return imeiNumber;
    }

    private void showSnackbar(String s) {
        snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Ulangi", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "OKE", Toast.LENGTH_SHORT).show();
                getOneAdruser();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
        snackbar.show();
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

    /*private void alertDialogInformasiError(String s) {
        Log.i(MY_LOG, "alertDialogInformasi");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_informasi, null);
        TextView tvInformasi = (TextView) view.findViewById(R.id.tvInformasi);
        tvInformasi.setText(s);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Informasi");
        adb.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(MY_LOG, "alertDialogInformasi setPositiveButton");
                finish();
            }
        });
        adb.setView(view);
        adi = adb.create();
        adi.show();
    }*/

    private void alertDialogIMEI(String response) {
        Log.i(MY_LOG, "alertDialogIMEI");
        if (response.equals("null")) {
            Log.i(MY_LOG, "alertDialogIMEI null");
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.alert_dialog_imei, null);
            tvImei = (TextView) view.findViewById(R.id.tvImei);
            tvImei.setText(imeiNumber);

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Informasi");
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

    private String getNippos() {
        Log.i(MY_LOG, "getNippos");
        String s = tvAnippos.getText().toString();
        return s;
    }

    private void showOneAdruser(String json) {
        try {
            Log.i(MY_LOG, "parseJSON try");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(DBConfig.TAG_JSON_ARRAY);
            JSONObject jo = jsonArray.getJSONObject(0);

            anippos = new String[jsonArray.length()];
            anama = new String[jsonArray.length()];

            anippos[0] = jo.getString(DBConfig.TAG_ANIPPOS);
            anama[0] = jo.getString(DBConfig.TAG_ANAMA);

            tvAnippos.setText(anippos[0]);
            tvAnama.setText(anama[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupTabIcons() {
        Log.i(MY_LOG, "setupTabIcons");
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        /*View view1 = getLayoutInflater().inflate(R.layout.antaran_tab_icon, null);
        view1.findViewById(R.id.iconId).setBackgroundResource(R.drawable.icon_tab1_selector);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        View view2 = getLayoutInflater().inflate(R.layout.antaran_tab_icon, null);
        view2.findViewById(R.id.iconId).setBackgroundResource(R.drawable.icon_tab2_selector);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        View view3 = getLayoutInflater().inflate(R.layout.antaran_tab_icon, null);
        view3.findViewById(R.id.iconId).setBackgroundResource(R.drawable.icon_tab3_selector);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));*/

        tabLayout.addTab(tabLayout.newTab().setText("Delivery Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Berhasil"));
        tabLayout.addTab(tabLayout.newTab().setText("Gagal"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setupViewPager() {
        Log.i(MY_LOG, "setupViewPager");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
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

    public void selectFragment(int position) {
        Log.i(MY_LOG, "selectFragment");
        viewPager.setCurrentItem(position, true);
    }

    @Override
    public void onBackPressed() {
        Log.i(MY_LOG, "onBackPressed");
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
