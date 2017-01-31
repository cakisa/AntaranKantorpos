package com.horirevens.antarankantorpos.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.horirevens.antarankantorpos.BarcodeScannerActivity;
import com.horirevens.antarankantorpos.DBConfig;
import com.horirevens.antarankantorpos.KolektifActivity;
import com.horirevens.antarankantorpos.LapDOActivity;
import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.about.AboutActivity;
import com.horirevens.antarankantorpos.antaran.Antaran;
import com.horirevens.antarankantorpos.antaran.AntaranAdapterDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by horirevens on 1/18/17.
 */
public class AntaranTab1 extends Fragment implements ListView.OnItemClickListener,
        ListView.OnItemLongClickListener {
    public static final String STR_ERROR = "Gagal memuat data";
    public static final String STR_BERHASIL = "Berhasil update status No Resi ";
    public static final String MY_LOG = "log_AntaranTab1";

    private View rootView;
    private ListView listView;
    private String anippos, valAkditem, valKeteranganStatus, valAstatus, valAketerangan;
    private CircularProgressView spinner, spinnerAstatus;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RadioGroup radioGroup;
    private FrameLayout frameNoData;
    private Snackbar snackbar;
    private AlertDialog adus, adjs, adp, adk;
    private MenuItem myItem;
    private SearchView searchView;
    private FloatingActionButton fab;
    private TextView tvCountData;

    private int animationDuration, countData;

    private AwesomeValidation awesomeValidation;
    private AntaranAdapterDO antaranAdapterDO;
    private ArrayList<Antaran> antaranList = new ArrayList<>();
    private ArrayList<String> astatusList = new ArrayList<>();
    private ArrayList<String> aketeranganList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(MY_LOG, "onCreateView");
        rootView = inflater.inflate(R.layout.tab_layout_antaran, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        spinner = (CircularProgressView) rootView.findViewById(R.id.spinner);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        frameNoData = (FrameLayout) rootView.findViewById(R.id.frameNoData);
        tvCountData = (TextView) rootView.findViewById(R.id.countData);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        anippos = getArguments().getString("anippos");
        frameNoData.setVisibility(View.GONE);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setVisibility(View.GONE);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        getAllAdrantaran();
        swipeRefresh();
        getIntentResult();

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.i(MY_LOG, "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            Log.i(MY_LOG, "setUserVisibleHint isVisible");
            listView.setVisibility(View.GONE);
            frameNoData.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            spinner.setAlpha(0f);
            spinner.setVisibility(View.VISIBLE);
            spinner.animate().alpha(1f).setDuration(animationDuration).setListener(null);
            getAllAdrantaran();
            if (!isVisibleToUser)
                Log.i(MY_LOG, "setUserVisibleHint !isVisibleToUser");
        }
    }

    private void getAllAdrantaran() {
        Log.i(MY_LOG, "getAllAdrantaran");
        String param1 = "?status=0";
        String param2 = "&anippos=" + anippos;
        String params = param1 + param2;
        Log.i(MY_LOG, "params: " + params);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void initListAdrantaran(String json) {
        try {
            Log.i(MY_LOG, "listAdrantaran 1");
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
                Log.i(MY_LOG, "listAdrantaran: " + valAkditem + ", " +
                    valAwktlokal + ", " + valAkdstatus + ", " + valAda_aketerangan + ", " +
                    valAds_aketerangan + ", " + valAstatuskirim + ", " + valAdo);

                antaranList.add(new Antaran(valAkditem, valAkdstatus, valAwktlokal, valAda_aketerangan,
                        valAds_aketerangan, valAstatuskirim, valAdo));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAllAdrantaran(String json) {
        Log.i(MY_LOG, "showAdrantaran");
        initListAdrantaran(json);
        antaranAdapterDO = new AntaranAdapterDO(antaranList, getContext());

        if (antaranAdapterDO.getCount() == 0) {
            frameNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        } else {
            frameNoData.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            listView.setAdapter(antaranAdapterDO);
            countData = listView.getAdapter().getCount();
            tvCountData.setText("" + countData);
        }
    }

    private void showSnackbar(String s, String se) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout);

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

        if (se.equals("00")) {
            snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("ulangi", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    barcodeScannerActivity();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(MY_LOG, "onCreateOptionsMenu");
        menu.clear();
        inflater.inflate(R.menu.tab_antaran, menu);

        searchView = (SearchView) menu.findItem(R.id.searchAkditem).getActionView();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchAkditem:
                Log.i(MY_LOG, "onOptionsItemSelected searchAkditem");
                searchAkditem(item);
                myItem = item;
                return true;
            case R.id.scanAkditem:
                Log.i(MY_LOG, "onOptionsItemSelected scanAkditem");
                barcodeScannerActivity();
                return true;
            case R.id.updateKolektif:
                Log.i(MY_LOG, "onOptionsItemSelected updateKolektif");
                kolektifActivity();
                return true;
            case R.id.laporanDO:
                Log.i(MY_LOG, "onOptionsItemSelected laporanDO");
                lapDOActivity();
                return true;
            case R.id.versi:
                Log.i(MY_LOG, "onOptionsItemSelected versi");
                aboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void barcodeScannerActivity() {
        Intent intent = new Intent(getActivity(), BarcodeScannerActivity.class);
        intent.putExtra("anippos", anippos);
        startActivity(intent);
    }

    private void kolektifActivity() {
        Intent intent = new Intent(getActivity(), KolektifActivity.class);
        intent.putExtra("anippos", anippos);
        startActivity(intent);
    }

    private void lapDOActivity() {
        Intent intent = new Intent(getActivity(), LapDOActivity.class);
        intent.putExtra("anippos", anippos);
        startActivity(intent);
    }

    private void aboutActivity() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }

    private void getIntentResult() {
        Log.i(MY_LOG, "getIntentResult");
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("resultScan")) {
            String resultScan = intent.getStringExtra("resultScan");
            if (resultScan.equals("null")) {
                String s = "No Barcode tidak ditemukan pada Delivery Order";
                String se = "1";
                showSnackbar(s, se);
            } else if (resultScan.equals("error")) {
                String s = "Gagal memuat data pada saat melakukan scan";
                String se = "00";
                showSnackbar(s, se);
            } else {
                alertDialogUpdateStatus(resultScan);
            }
        }
    }

    private void searchAkditem(MenuItem item) {
        Log.i(MY_LOG, "searchAkditem");
        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (item != null) {
            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    Log.i(MY_LOG, "searchAkditem onMenuItemActionExpand");
                    if (searchView != null) {
                        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
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
                                antaranAdapterDO.filter(s);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(MY_LOG, "onItemClick");
        Antaran antaran = antaranList.get(position);
        String s = String.valueOf(antaran.getAkditem());
        //String s = String.valueOf(listView.getItemAtPosition(position));
        alertDialogUpdateStatus(s);

        if (adus.isShowing()) {
            Log.i(MY_LOG, "adus isShowing");
            if (!searchView.isIconified()) {
                Log.i(MY_LOG, "true");
                MenuItemCompat.collapseActionView(myItem);
            }
        }
    }

    private void alertDialogUpdateStatus(String s) {
        Log.i(MY_LOG, "alertDialogUpdateStatus");
        valAkditem = s;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_dialog_update_status, null);
        TextView tvValAkditem = (TextView) view.findViewById(R.id.tvValAkditem);
        tvValAkditem.setText(s);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_dialog_jenis_status, null);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        spinnerAstatus = (CircularProgressView) view.findViewById(R.id.spinnerAstatus);

        radioGroup.setVisibility(View.GONE);
        getAllAdrstatus(params);
        Log.i(MY_LOG, "alertDialogJenisStatus getAllAdrstatus");

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
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
                        antaranAdapterDO.notifyDataSetChanged();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
            RadioButton radioButton = new RadioButton(getContext());
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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_dialog_keterangan, null);
        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(getContext());

        final EditText keteranganStatus = (EditText) view.findViewById(R.id.keteranganStatus);
        int maxLength = 25;
        keteranganStatus.setFilters(new InputFilter[] {new InputFilter.AllCaps(), new InputFilter.LengthFilter(maxLength)});

        Log.i(MY_LOG, "initAwesomeValidation");
        awesomeValidation.addValidation(keteranganStatus, "[a-zA-Z\\s]+", getResources().getString(R.string.err_name));

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_dialog_validasi, null);
        TextView tvAkditem = (TextView) view.findViewById(R.id.tvAkditem);
        TextView tvAketerangan = (TextView) view.findViewById(R.id.tvAketerangan);
        TextView tvAnama = (TextView) view.findViewById(R.id.tvAnama);
        TextView tvOleh = (TextView) view.findViewById(R.id.tvOleh);

        if (status == "1") {
            Log.i(MY_LOG, "alertDialogValidasi berhasil");
            tvOleh.setText("Diterima Oleh");
            tvAkditem.setText(valAkditem);
            tvAketerangan.setText(valAketerangan);
            tvAnama.setText(valKeteranganStatus.toUpperCase());
        }

        if (status == "0") {
            Log.i(MY_LOG, "alertDialogValidasi gagal");
            tvOleh.setText("Keterangan Gagal");
            tvAkditem.setText(valAkditem);
            tvAketerangan.setText(valAketerangan);
            tvAnama.setText(valKeteranganStatus.toUpperCase());
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
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
        Log.i(MY_LOG, "updateData");
        Calendar c = Calendar.getInstance();

        int second = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String time = String.format("%02d:%02d:%02d", hour, minute, second);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = String.format("%04d-%02d-%02d", year, month, day);

        final String awktlokal = date+" "+time;

        listView.setVisibility(View.GONE);
        spinner.setAlpha(0f);
        spinner.setVisibility(View.VISIBLE);
        spinner.animate().alpha(1f).setDuration(animationDuration).setListener(null);

        String s = "?status=4";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBConfig.JSON_URL_ADRANTARAN + s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MY_LOG, "updateData onResponse");
                        String se = "1";
                        showSnackbar(STR_BERHASIL + valAkditem, se);
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
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(DBConfig.KEY_AKDITEM, valAkditem);
                params.put(DBConfig.KEY_ANIPPOS, anippos);
                params.put(DBConfig.KEY_AKDSTATUS, valAstatus);
                params.put(DBConfig.KEY_AWKTLOKAL, awktlokal);
                params.put(DBConfig.KEY_AKETERANGAN, valKeteranganStatus);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(MY_LOG, "onItemLongClick");
        Toast.makeText(getContext(), "Long click", Toast.LENGTH_SHORT).show();
        return true;
    }
}
