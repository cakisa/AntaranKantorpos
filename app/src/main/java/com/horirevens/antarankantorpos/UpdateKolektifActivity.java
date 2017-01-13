package com.horirevens.antarankantorpos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.horirevens.antarankantorpos.antaran.AdrstatusParseJSON;
import com.horirevens.antarankantorpos.antaran.AntaranAdapterKolektif;
import com.horirevens.antarankantorpos.antaran.AntaranParseJSON;
import com.horirevens.antarankantorpos.libs.MyCustomRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by horirevens on 12/27/16.
 */
public class UpdateKolektifActivity extends AppCompatActivity {
    public static final String MY_LOG = "log_UpdateKolektif";
    public static final String JSON_URL_ADRANTARAN = "http://mob.agenposedo.com/adrantaran.php";
    public static final String JSON_URL_ADRSTATUS = "http://mob.agenposedo.com/adrstatus.php";

    public static final String KEY_AKDITEM = "akditem";
    public static final String KEY_ANIPPOS = "anippos";
    public static final String KEY_AWKTLOKAL = "awktlokal";
    public static final String KEY_AKDSTATUS = "akdstatus";
    public static final String KEY_AKETERANGAN = "aketerangan";

    private ListView listView;
    private Toolbar toolbar;
    private String anippos, akditem, valKeteranganStatus, valAstatus, valAketerangan;
    private CircularProgressView spinner, spinnerAstatus;
    private FrameLayout frameNoData;
    private SearchView searchView;
    private RadioGroup radioGroup;
    private FloatingActionButton fab;
    private AlertDialog adus, adjs, adp, adk, adi;

    private int animationDuration, checkedItem;

    private ArrayList<String> checkboxList = new ArrayList<>();
    private AntaranAdapterKolektif antaranAdapterKolektif;
    private AwesomeValidation awesomeValidation;
    private RequestQueue requestQueue;
    private MyCustomRequest myCustomRequest;
    private JsonObjectRequest jsonRequest;
    private Map<String, String> map = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antaran_update_kolektif);
        Log.i(MY_LOG, "onCreate");

        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        spinner = (CircularProgressView) findViewById(R.id.spinner);
        listView = (ListView) findViewById(R.id.listView);
        frameNoData = (FrameLayout) findViewById(R.id.frameNoData);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        frameNoData.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        getIntentResult();
        getAllAdrantaran();
        initCheckBox();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_kolektif, menu);
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

    private void getIntentResult() {
        Log.i(MY_LOG, "getIntentResult");
        Intent intent = getIntent();
        if (intent.hasExtra("anippos")) {
            anippos = intent.getStringExtra("anippos");
        }
    }

    private void searchAkditem(MenuItem item) {
        Log.i(MY_LOG, "searchAkditem");
        final SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);

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
                                if (!TextUtils.isEmpty(s)) {
                                    akditem = s;
                                    getAllAdrantaran();
                                } else {
                                    akditem = null;
                                    getAllAdrantaran();
                                }
                                return false;
                            }
                        });
                    }
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    Log.i(MY_LOG, "searchAkditem onMenuItemActionCollapse");
                    akditem = null;
                    return true;
                }
            });

            searchView = (SearchView) item.getActionView();
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
                        String s = "Gangguan koneksi. Sedang menunggu jaringan...";
                        alertDialogInformasi(s);
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
        antaranAdapterKolektif = new AntaranAdapterKolektif(
                this, AntaranParseJSON.akditem, AntaranParseJSON.awklokal, AntaranParseJSON.adrsAketerangan, AntaranParseJSON.length);
        antaranAdapterKolektif.notifyDataSetChanged();

        if (antaranAdapterKolektif.getCount() == 0) {
            frameNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        } else {
            frameNoData.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            listView.setAdapter(antaranAdapterKolektif);
        }
    }

    private void initCheckBox() {
        Log.i(MY_LOG, "initCheckBox");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxList.clear();
                for (int i = 0; i < antaranAdapterKolektif.getCount(); i++) {
                    if (antaranAdapterKolektif.getItemChecked(i)) {
                        checkboxList.add(antaranAdapterKolektif.getItem(i));
                    }
                }

                alertDialogUpdateStatus(checkboxList.size());
                checkedItem = checkboxList.size();
            }
        });
    }

    private void alertDialogUpdateStatus(int j) {
        Log.i(MY_LOG, "alertDialogUpdateStatus");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_update_status, null);
        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        TextView tvValAkditem = (TextView) view.findViewById(R.id.tvValAkditem);

        tvLabel.setText("Jumlah Resi");
        tvValAkditem.setText("" + j);

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
        /*adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.i(MY_LOG, "alertDialogUpdateStatus onDismiss");
                getAllAdrantaran();
            }
        });*/
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
        adb.setTitle("Keterangan");
        /*adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.i(MY_LOG, "alertDialogJenisStatus onDismiss");
                getAllAdrantaran();
            }
        });*/
        adb.setView(view);
        adjs = adb.create();
        adjs.show();
    }

    private void getAllAdrstatus(String params) {
        Log.i(MY_LOG, "getAllAdrstatus");
        StringRequest stringRequest = new StringRequest(JSON_URL_ADRSTATUS + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MY_LOG, "getAllAdrstatus onResponse");
                        radioGroup.setAlpha(0f);
                        radioGroup.setVisibility(View.VISIBLE);
                        radioGroup.animate().alpha(1f).setDuration(animationDuration).setListener(null);
                        addRadioButton(response);

                        spinnerAstatus.animate().alpha(0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                spinnerAstatus.setVisibility(View.GONE);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MY_LOG, "onErrorResponse");
                        String s = "Gangguan koneksi. Sedang menunggu jaringan...";
                        alertDialogInformasi(s);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addRadioButton(String json) {
        Log.i(MY_LOG, "addRadioButton");
        AdrstatusParseJSON apj = new AdrstatusParseJSON(json);
        apj.parseJSON();
        Log.i(MY_LOG, "addRadioButton parseJSON");

        for (int i = 0; i < AdrstatusParseJSON.jsonArrayLength; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(Integer.parseInt(AdrstatusParseJSON.astatus[i]));
            radioButton.setText(AdrstatusParseJSON.aketerangan[i]);
            radioButton.setTextSize(18);
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
                                rbIdValue.equals("6215") || rbIdValue.equals("6216") ||
                                rbIdValue.equals("6218")) {
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
        keteranganStatus.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Log.i(MY_LOG, "initAwesomeValidation");
        awesomeValidation.addValidation(keteranganStatus, "[a-zA-Z\\s]+", getResources().getString(R.string.err_name));

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        if (status.equals("1")) {
            adb.setTitle("Nama Penerima");
        } else {
            adb.setTitle("Keterangan Gagal");
        }
        adb.setPositiveButton("Simpan", null);
        /*adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.i(MY_LOG, "alertDialogKeterangan onDismiss");
                getAllAdrantaran();
            }
        });*/
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

    /*adb.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                awesomeValidation.validate();
                Log.i(MY_LOG, "validate");

                Log.i(MY_LOG, "alertDialogPenerima simpan");
                //String valNamaPenerima = namaPenerima.getText().toString().trim();
                //resAnama[0] = valNamaPenerima;
                //adp.dismiss();
                //alertDialogValidasi(status);
            }
        });
        adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                namaPenerima.setText("");
            }
        });
        adb.setView(view);
        adp = adb.create();
        adp.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        adp.show();*/
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
            tvAkditem.setText("" + checkboxList.size());
            tvAketerangan.setText(valAketerangan);
            tvAnama.setText(valKeteranganStatus.toUpperCase());
        }

        if (status == "0") {
            Log.i(MY_LOG, "alertDialogValidasi gagal");
            tvOleh.setText("Keterangan Gagal");
            tvAkditem.setText("" + checkboxList.size());
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
        /*adb.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.i(MY_LOG, "alertDialogValidasi onDismiss");
                getAllAdrantaran();
            }
        });*/
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
                jsonObject.put(KEY_AKDITEM, checkboxList.get(i));
                jsonObject.put(KEY_ANIPPOS, anippos);
                jsonObject.put(KEY_AKDSTATUS, valAstatus);
                jsonObject.put(KEY_AWKTLOKAL, awktlokal);
                jsonObject.put(KEY_AKETERANGAN, valKeteranganStatus);
                jsonArray.put(jsonObject);
            }

            JSONObject jsonObjectArray = new JSONObject();
            jsonObjectArray.put("array", jsonArray);
            final String jsonString = jsonObjectArray.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    JSON_URL_ADRANTARAN + s,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(MY_LOG, "updateData onResponse");
                            String s = "Berhasil update status";
                            alertDialogInformasi(s);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(MY_LOG, "onErrorResponse");
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
                        // can get more details such as response.headers
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

    private void alertDialogInformasi(String s) {
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
                getAllAdrantaran();
                adi.dismiss();
            }
        });
        adb.setView(view);
        adi = adb.create();
        adi.show();
    }
}
