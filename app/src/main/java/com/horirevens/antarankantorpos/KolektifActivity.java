package com.horirevens.antarankantorpos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.horirevens.antarankantorpos.antaran.AntaranKolektif;
import com.horirevens.antarankantorpos.antaran.AntaranKolektifAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

public class KolektifActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener {
    public static final String STR_ERROR = "Gagal memuat data";
    public static final String STR_BERHASIL = "Berhasil update status No Resi ";
    public static final String MY_LOG = "log_KolektifActivity";

    private ListView listView;
    private String anippos, valKeteranganStatus, valAstatus, valAketerangan;
    private CircularProgressView spinner, spinnerAstatus;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RadioGroup radioGroup;
    private FrameLayout frameNoData;
    private Snackbar snackbar;
    private AlertDialog adus, adjs, adp, adk;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private SearchView searchView;
    private String networkStatus;
    private TextView tvTitle, tvNetwork;

    private int animationDuration, checkedItem;

    private AwesomeValidation awesomeValidation;
    private AntaranKolektifAdapter antaranKolektifAdapter;
    private ArrayList<AntaranKolektif> antaranList = new ArrayList<>();
    private ArrayList<String> astatusList = new ArrayList<>();
    private ArrayList<String> aketeranganList = new ArrayList<>();
    private ArrayList<String> checkedList = new ArrayList<>();

    /*private IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MY_LOG, "onReceive");
            networkStatus = NetworkStatus.getConnectivityStatusString(context.getApplicationContext());

            if (networkStatus == "1") {
                tvNetwork.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
            }
            if (networkStatus == "0") {
                tvNetwork.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.GONE);

                Snackbar.make(coordinatorLayout, "Tidak ada koneksi internet", Snackbar.LENGTH_LONG).show();
            }
        }
    };*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(MY_LOG, "onCreateView");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kolektif);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvNetwork = (TextView) findViewById(R.id.tvNetwork);
        listView = (ListView) findViewById(R.id.listView);
        spinner = (CircularProgressView) findViewById(R.id.spinner);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        frameNoData = (FrameLayout) findViewById(R.id.frameNoData);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        frameNoData.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listView);
        listView.setVisibility(View.GONE);
        fab.setOnClickListener(this);

        getIntentResult();
        getAllAdrantaran();
        swipeRefresh();
    }

    private void getIntentResult() {
        Log.i(MY_LOG, "getIntentResult");
        Intent intent = getIntent();
        if (intent.hasExtra("anippos")) {
            anippos = intent.getStringExtra("anippos");
        }
    }

    private void getAllAdrantaran() {
        Log.i(MY_LOG, "getAllAdrantaran");
        String param1 = "?status=0";
        String param2 = "&anippos=" + anippos;
        String params = param1 + param2;
        StringRequest stringRequest = new StringRequest(DBConfig.JSON_URL_ADRANTARAN + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MY_LOG, "onResponse");
                        listView.setAlpha(0f);
                        listView.setVisibility(View.VISIBLE);
                        listView.animate().alpha(1f).setDuration(animationDuration).setListener(null);
                        antaranList.clear();
                        showAllAdrantaran(response);

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

    private void initListAdrantaran(String json) {
        try {
            Log.i(MY_LOG, "listAdrantaran");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(DBConfig.TAG_JSON_ARRAY);

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String valAkditem = jo.getString(DBConfig.TAG_AKDITEM);
                String valAwktlokal = jo.getString(DBConfig.TAG_AWKTLOKAL);
                String valAkdstatus = jo.getString(DBConfig.TAG_AKDSTATUS);
                String valAda_aketerangan = jo.getString(DBConfig.TAG_ADRA_AKETERANGAN);
                String valAds_aketerangan = jo.getString(DBConfig.TAG_ADRS_AKETERANGAN);
                String valAstatuskirim = jo.getString(DBConfig.TAG_ASTATUSKIRIM);
                String valAdo = jo.getString(DBConfig.TAG_ADO);
                boolean valSelected = false;
                Log.i(MY_LOG, "listAdrantaran: " + valAkditem + ", " +
                        valAwktlokal + ", " + valAkdstatus + ", " + valAda_aketerangan + ", " +
                        valAds_aketerangan + ", " + valAstatuskirim + ", " + valAdo);

                antaranList.add(new AntaranKolektif(valAkditem, valAkdstatus, valAwktlokal, valAda_aketerangan,
                        valAds_aketerangan, valAstatuskirim, valAdo, valSelected));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAllAdrantaran(String json) {
        Log.i(MY_LOG, "showAdrantaran");
        initListAdrantaran(json);
        antaranKolektifAdapter = new AntaranKolektifAdapter(antaranList, this);

        if (antaranKolektifAdapter.getCount() == 0) {
            frameNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            //fab.setVisibility(View.GONE);
        } else {
            frameNoData.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            //fab.setVisibility(View.VISIBLE);

            listView.setAdapter(antaranKolektifAdapter);
            //countData = listView.getAdapter().getCount();
            //tvCountData.setText("" + countData);
        }
    }

    private void showSnackbar(String s, String se) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        if (se.equals("0")) {
            snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("ulangi", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAllAdrantaran();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(MY_LOG, "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.kolektif, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchAkditem:
                Log.i(MY_LOG, "onOptionsItemSelected searchAkditem");
                searchAkditem(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchAkditem(MenuItem item) {
        Log.i(MY_LOG, "searchAkditem");
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (item != null) {
            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    Log.i(MY_LOG, "searchAkditem onMenuItemActionExpand");
                    if (searchView != null) {
                        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                        searchView.setIconified(false);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {
                                Log.i(MY_LOG, "searchAkditem onQueryTextChange");
                                antaranKolektifAdapter.filter(s);
                                listView.invalidate();
                                return false;
                            }
                        });
                    }
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    Log.i(MY_LOG, "searchAkditem onMenuItemActionCollapse");
                    return true;
                }
            });

            searchView = (SearchView) item.getActionView();
        }
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
                        getAllAdrantaran();
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

    private void alertDialogUpdateStatus(int s) {
        Log.i(MY_LOG, "alertDialogUpdateStatus");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_update_status, null);
        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        TextView tvValAkditem = (TextView) view.findViewById(R.id.tvValAkditem);

        tvLabel.setText("Jumlah Resi");
        tvValAkditem.setText("" + s);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Update Status");
        adb.setPositiveButton("Berhasil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(MY_LOG, "alertDialogUpdateStatus Berhasil");
                String params = "?status=1";
                alertDialogJenisStatus(params);
            }
        });
        adb.setNegativeButton("Gagal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(MY_LOG, "alertDialogUpdateStatus Gagal");
                String params = "?status=0";
                alertDialogJenisStatus(params);
            }
        });
        adb.setView(view);
        adus = adb.create();
        adus.show();
    }

    private void alertDialogJenisStatus(String params) {
        Log.i(MY_LOG, "alertDialogJenisStatus");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_jenis_status, null);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        spinnerAstatus = (CircularProgressView) view.findViewById(R.id.spinnerAstatus);

        radioGroup.setVisibility(View.GONE);
        getAllAdrstatus(params);
        Log.i(MY_LOG, "alertDialogJenisStatus getAllAdrstatus");

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Status Kiriman");
        adb.setView(view);
        adjs = adb.create();
        adjs.show();
    }

    private void getAllAdrstatus(String params) {
        Log.i(MY_LOG, "getAllAdrstatus");
        StringRequest stringRequest = new StringRequest(DBConfig.JSON_URL_ADRSTATUS + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MY_LOG, "getAllAdrstatus onResponse");
                        radioGroup.setAlpha(0f);
                        radioGroup.setVisibility(View.VISIBLE);
                        radioGroup.animate().alpha(1f).setDuration(animationDuration).setListener(null);
                        astatusList.clear();
                        aketeranganList.clear();
                        addRadioButton(response);

                        spinnerAstatus.animate().alpha(0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                spinnerAstatus.setVisibility(View.GONE);
                            }
                        });
                        antaranKolektifAdapter.notifyDataSetChanged();
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

    private void initListAdrstatus(String json) {
        try {
            Log.i(MY_LOG, "initListAdrstatus");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(DBConfig.TAG_JSON_ARRAY);

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String valAstatus = jo.getString(DBConfig.TAG_ASTATUS);
                String valAketerangan = jo.getString(DBConfig.TAG_AKETERANGAN);

                astatusList.add(valAstatus);
                aketeranganList.add(valAketerangan);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addRadioButton(String json) {
        Log.i(MY_LOG, "addRadioButton");
        initListAdrstatus(json);

        for (int i = 0; i < astatusList.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(Integer.parseInt(astatusList.get(i)));
            radioButton.setText(aketeranganList.get(i));
            radioButton.setTextSize(16);
            radioGroup.addView(radioButton);
        }
        Log.i(MY_LOG, "addRadioButton showOption");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                int childCount = radioGroup.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(x);
                    if (radioButton.getId() == checkedId) {
                        Log.i(MY_LOG, "addRadioButton clicked");
                        adjs.dismiss();

                        String rbIdValue = String.valueOf(radioButton.getId());
                        String rbTextValue = (String) radioButton.getText();
                        valAstatus = rbIdValue;
                        valAketerangan = rbTextValue;

                        if (rbIdValue.equals("6207") || rbIdValue.equals("6208") ||
                                rbIdValue.equals("6209") || rbIdValue.equals("6210") ||
                                rbIdValue.equals("6211") || rbIdValue.equals("6212") ||
                                rbIdValue.equals("6213") || rbIdValue.equals("6214") ||
                                rbIdValue.equals("6215") || rbIdValue.equals("6216") ||
                                rbIdValue.equals("6217") || rbIdValue.equals("6218")) {
                            Log.i(MY_LOG, "addRadioButton berhasil");
                            String status = "1";
                            alertDialogKeterangan(status);
                        }

                        if (rbIdValue.equals("6221") || rbIdValue.equals("6220") ||
                                rbIdValue.equals("6238")) {
                            Log.i(MY_LOG, "addRadioButton gagal");
                            String status = "0";
                            alertDialogKeterangan(status);
                        }
                    }
                }
            }
        });
    }

    private void alertDialogKeterangan(final String status) {
        Log.i(MY_LOG, "alertDialogKeterangan");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_keterangan, null);
        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(getApplicationContext());

        final EditText keteranganStatus = (EditText) view.findViewById(R.id.keteranganStatus);
        int maxLength = 25;
        keteranganStatus.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(maxLength)});

        Log.i(MY_LOG, "initAwesomeValidation");
        awesomeValidation.addValidation(keteranganStatus, "[a-zA-Z\\s]+", getResources().getString(R.string.err_name));

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        if (status.equals("1")) {
            adb.setTitle("Nama Penerima");
        } else {
            adb.setTitle("Keterangan Gagal");
        }
        adb.setPositiveButton("Simpan", null);
        adb.setView(view);
        adp = adb.create();
        adp.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btn = adp.getButton(AlertDialog.BUTTON_POSITIVE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(MY_LOG, "alertDialogKeterangan simpan");
                        if (awesomeValidation.validate()) {
                            Log.i(MY_LOG, "validate");
                            String a = keteranganStatus.getText().toString().trim();
                            valKeteranganStatus = a;
                            adp.dismiss();
                            alertDialogValidasi(status);
                        }
                    }
                });
            }
        });
        adp.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        adp.show();
    }

    private void alertDialogValidasi(final String status) {
        Log.i(MY_LOG, "alertDialogValidasi");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_validasi, null);
        TextView tvAkditem = (TextView) view.findViewById(R.id.tvAkditem);
        TextView tvAketerangan = (TextView) view.findViewById(R.id.tvAketerangan);
        TextView tvAnama = (TextView) view.findViewById(R.id.tvAnama);
        TextView tvOleh = (TextView) view.findViewById(R.id.tvOleh);
        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);

        tvLabel.setText("Jumlah Resi");

        if (status == "1") {
            Log.i(MY_LOG, "alertDialogValidasi berhasil");
            tvOleh.setText("Diterima Oleh");
            tvAkditem.setText("" + checkedList.size());
            tvAketerangan.setText(valAketerangan);
            tvAnama.setText(valKeteranganStatus.toUpperCase());
        }

        if (status == "0") {
            Log.i(MY_LOG, "alertDialogValidasi gagal");
            tvOleh.setText("Keterangan Gagal");
            tvAkditem.setText("" + checkedList.size());
            tvAketerangan.setText(valAketerangan);
            tvAnama.setText(valKeteranganStatus.toUpperCase());
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Validasi Update Status");
        adb.setPositiveButton("Validasi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(MY_LOG, "alertDialogValidasi Validasi");
                updateData();
            }
        });
        adb.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(MY_LOG, "alertDialogValidasi onClick");
                adk.dismiss();
            }
        });
        adb.setView(view);
        adk = adb.create();
        adk.show();
    }

    private void updateData() {
        try {
            Log.i(MY_LOG, "updateData");
            Calendar c = Calendar.getInstance();

            int second = c.get(Calendar.SECOND);
            int minute = c.get(Calendar.MINUTE);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            String time = String.format("%02d:%02d:%02d", hour, minute, second);

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            String date = String.format("%04d-%02d-%02d", year, month, day);

            final String awktlokal = date + " " + time;

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String s = "?status=5";
            JSONArray jsonArray = new JSONArray();

            for (int i=0; i<checkedItem; i++) {
                Log.i(MY_LOG, "looping " + i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(DBConfig.KEY_AKDITEM, checkedList.get(i));
                jsonObject.put(DBConfig.KEY_ANIPPOS, anippos);
                jsonObject.put(DBConfig.KEY_AKDSTATUS, valAstatus);
                jsonObject.put(DBConfig.KEY_AWKTLOKAL, awktlokal);
                jsonObject.put(DBConfig.KEY_AKETERANGAN, valKeteranganStatus);
                jsonArray.put(jsonObject);
            }

            JSONObject jsonObjectArray = new JSONObject();
            jsonObjectArray.put("array", jsonArray);
            final String jsonString = jsonObjectArray.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    DBConfig.JSON_URL_ADRANTARAN + s,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(MY_LOG, "updateData onResponse");
                            String s = "Berhasil update status " + checkedList.size() + " item";
                            String se = "1";
                            showSnackbar(s, se);
                            getAllAdrantaran();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(MY_LOG, "onErrorResponse");
                            String se = "0";
                            showSnackbar(STR_ERROR, se);
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonString == null ? null : jsonString.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonString, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
            Log.i(MY_LOG, "json: " + jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
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
    }*/

    @Override
    public void onClick(View v) {
        checkedList.clear();
        for (int i = 0; i < antaranList.size(); i++) {
            AntaranKolektif antaranKolektif = antaranList.get(i);
            if (antaranKolektif.isSelected()) {
                checkedList.add(antaranKolektif.getAkditem());
            }
        }

        if (checkedList.size() == 0) {
            String s = "Tidak ada data yang dipilih";
            String se = "1";
            showSnackbar(s, se);
        } else {
            alertDialogUpdateStatus(checkedList.size());
            checkedItem = checkedList.size();
        }
    }
}
