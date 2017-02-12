package com.horirevens.antarankantorpos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by horirevens on 1/19/17.
 */
public class BarcodeScannerActivity extends AppCompatActivity {
    public static final String MY_LOG = "log_BarcodeScanner";

    private String anippos, akditem, resAkditem;

    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MY_LOG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        getIntentResult();
        scanBarcode();
    }

    private void getIntentResult() {
        Log.i(MY_LOG, "getIntentResult");
        Intent intent = getIntent();
        if (intent.hasExtra("anippos")) {
            anippos = intent.getStringExtra("anippos");
        }
    }

    private void scanBarcode() {
        Log.i(MY_LOG, "scanBarcode");
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        scanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        scanIntegrator.setPrompt("Scan No Barcode");
        scanIntegrator.setOrientationLocked(true);
        scanIntegrator.setBeepEnabled(false);
        scanIntegrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(MY_LOG, "onActivityResult");
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null) {
            Log.i(MY_LOG, ""+akditem);
            akditem = scanningResult.getContents();
            cekAkditem(akditem, anippos);
        }
    }

    private void cekAkditem(String akditem, String anippos) {
        Log.i(MY_LOG, "cekAkditem");
        String param1 = "?status=3";
        String param2 = "&anippos=" + anippos;
        String param3 = "&akditem=" + akditem;
        String params = param1 + param2 + param3;
        Log.i(MY_LOG, params);
        StringRequest stringRequest = new StringRequest(DBConfig.JSON_URL_ADRANTARAN + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MY_LOG, "onResponse");
                        resultParseJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MY_LOG, "onErrorResponse");
                        Intent maIntent = new Intent(getApplicationContext(), LaunchActivity.class);
                        startActivity(maIntent);
                        finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void resultParseJSON(String response) {
        try {
            Log.i(MY_LOG, "parseJSON");
            if (response.equals("null")) {
                Log.i(MY_LOG, "parseJSON null");
                String resultScan = "null";
                startLaunchActivity(resultScan);
            } else {
                Log.i(MY_LOG, "parseJSON try");
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(DBConfig.TAG_JSON_ARRAY);
                JSONObject jo = jsonArray.getJSONObject(0);
                resAkditem = jo.getString(DBConfig.TAG_AKDITEM);
                Log.i(MY_LOG, ""+resAkditem);

                String resultScan = resAkditem;
                startLaunchActivity(resultScan);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startLaunchActivity(String resultScan) {
        Log.i(MY_LOG, "startLaunchActivity");
        Intent maIntent = new Intent(this, LaunchActivity.class);
        maIntent.putExtra("resultScan", resultScan);
        startActivity(maIntent);
        finish();
    }
}
