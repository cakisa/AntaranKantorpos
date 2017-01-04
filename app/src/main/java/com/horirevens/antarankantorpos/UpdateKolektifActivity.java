package com.horirevens.antarankantorpos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.horirevens.antarankantorpos.antaran.AntaranAdapter;
import com.horirevens.antarankantorpos.antaran.AntaranAdapterCheckbox;
import com.horirevens.antarankantorpos.antaran.AntaranParseJSON;

/**
 * Created by horirevens on 12/27/16.
 */
public class UpdateKolektifActivity extends AppCompatActivity {
    public static final String MY_LOG = "log_SearchWithCheckbox";
    public static final String JSON_URL_ADRANTARAN = "http://mob.agenposedo.com/adrantaran.php";

    private ListView listView;
    private String anippos, akditem;
    private ProgressBar spinner;
    private AntaranAdapterCheckbox antaranAdapterCheckbox;

    private int animationDuration;

    AntaranAdapter antaranAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antaran_checklist);
        Log.i(MY_LOG, "onCreate");

        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        spinner = (ProgressBar) findViewById(R.id.spinner);
        listView = (ListView) findViewById(R.id.listView);

        getIntentResult();
        getAllAdrantaran();
    }

    private void getIntentResult() {
        Log.i(MY_LOG, "getIntentResult");
        Intent intent = getIntent();
        if (intent.hasExtra("anippos")) {
            anippos = intent.getStringExtra("anippos");
            //Toast.makeText(this, anippos, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllAdrantaran() {
        Log.i(MY_LOG, "getAllAdrantaran");
        String param1 = "?status=0";
        String param2 = "&anippos=" + anippos;
        String param3 = "&akditem=" + akditem;
        String params = param1 + param2 + param3;
        StringRequest stringRequest = new StringRequest(JSON_URL_ADRANTARAN + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MY_LOG, "onResponse");
                        listView.setAlpha(0f);
                        listView.setVisibility(View.VISIBLE);
                        listView.animate().alpha(1f).setDuration(animationDuration).setListener(null);
                        showAdrantaran(response);

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
                        Toast.makeText(getApplicationContext(), "Kesalahan Pada Pengambilan Data", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showAdrantaran(String json) {
        Log.i(MY_LOG, "showAdrantaran");
        AntaranParseJSON pj = new AntaranParseJSON(json);
        pj.parseJSON();
        Log.i(MY_LOG, "showAdrantaran parseJSON");
        antaranAdapter = new AntaranAdapter(
                this, AntaranParseJSON.akditem, AntaranParseJSON.akdstatus, AntaranParseJSON.awklokal,
                AntaranParseJSON.adraAketerangan, AntaranParseJSON.adrsAketerangan, AntaranParseJSON.astatuskirim);
        antaranAdapter.notifyDataSetChanged();
        listView.setAdapter(antaranAdapter);
    }
}
