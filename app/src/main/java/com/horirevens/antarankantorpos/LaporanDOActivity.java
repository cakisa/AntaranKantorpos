package com.horirevens.antarankantorpos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.horirevens.antarankantorpos.antaran.AntaranAdapterLaporanDO;
import com.horirevens.antarankantorpos.antaran.AntaranLapDOParseJSON;

/**
 * Created by horirevens on 1/15/17.
 */
public class LaporanDOActivity extends AppCompatActivity {
    public static final String MY_LOG = "log_LaporanDO";
    public static final String STR_ERROR = "Gagal memuat data";

    private ListView listView;
    private Toolbar toolbar;
    private Snackbar snackbar;
    private String anippos;
    private CoordinatorLayout coordinatorLayout;
    private CircularProgressView spinner;
    private FrameLayout frameNoData;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int animationDuration;
    private AntaranAdapterLaporanDO antaranAdapterLaporanDO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antaran_laporan_do);
        Log.i(MY_LOG, "onCreate");

        listView = (ListView) findViewById(R.id.listView);
        frameNoData = (FrameLayout) findViewById(R.id.frameNoData);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner = (CircularProgressView) findViewById(R.id.spinner);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        frameNoData.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        getIntentResult();
        getAllLaporanDO();
        swipeRefresh();
    }

    private void swipeRefresh() {
        Log.i(MY_LOG, "swipeRefresh");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.setVisibility(View.GONE);
                        frameNoData.setVisibility(View.GONE);
                        getAllLaporanDO();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void getIntentResult() {
        Log.i(MY_LOG, "getIntentResult");
        Intent intent = getIntent();
        if (intent.hasExtra("anippos")) {
            anippos = intent.getStringExtra("anippos");
        }
    }

    private void getAllLaporanDO() {
        Log.i(MY_LOG, "getAllLaporanDO");
        String params = "?anippos=" + anippos;
        StringRequest stringRequest = new StringRequest(DBConfig.JSON_URL_ADO + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MY_LOG, "onResponse ");
                        listView.setAlpha(0f);
                        listView.setVisibility(View.VISIBLE);
                        listView.animate().alpha(1f).setDuration(animationDuration).setListener(null);
                        showLaporanDO(response);

                        spinner.animate().alpha(0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                spinner.setVisibility(View.GONE);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MY_LOG, "onErrorResponse");
                        String se = "0";
                        showSnackbar(STR_ERROR, se);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showLaporanDO(String json) {
        Log.i(MY_LOG, "showLaporanDO");
        AntaranLapDOParseJSON pj = new AntaranLapDOParseJSON(json);
        pj.parseJSON();
        Log.i(MY_LOG, "showLaporanDO parseJSON");
        antaranAdapterLaporanDO = new AntaranAdapterLaporanDO(
                this, AntaranLapDOParseJSON.ado, AntaranLapDOParseJSON.proses,
                AntaranLapDOParseJSON.berhasil, AntaranLapDOParseJSON.gagal,
                AntaranLapDOParseJSON.jml_item);
        antaranAdapterLaporanDO.notifyDataSetChanged();

        if (antaranAdapterLaporanDO.getCount() == 0) {
            frameNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            frameNoData.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            listView.setAdapter(antaranAdapterLaporanDO);
        }
    }

    private void showSnackbar(String s, String se) {
        if (se.equals("0")) {
            snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ulangi", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAllLaporanDO();
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        }

        if (se.equals("1")) {
            snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_LONG);
        }

        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
        snackbar.show();
    }
}
