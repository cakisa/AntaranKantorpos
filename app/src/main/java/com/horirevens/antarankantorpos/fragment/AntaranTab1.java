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
import android.text.TextUtils;
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
import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.UpdateKolektifActivity;
import com.horirevens.antarankantorpos.antaran.AdrstatusParseJSON;
import com.horirevens.antarankantorpos.antaran.AntaranAdapter;
import com.horirevens.antarankantorpos.antaran.AntaranParseJSON;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by horirevens on 12/16/16.
 */
public class AntaranTab1 extends Fragment implements ListView.OnItemClickListener, ListView.OnItemLongClickListener {
    public static final String STR_ERROR = "Gagal memuat data";
    public static final String STR_BERHASIL = "Berhasil update status No Resi ";

    public static final String MY_LOG = "log_AntaranTab1";

    private ListView listView;
    private View rootView;
    private TextView tvCountData;
    private String anippos, akditem, valAkditem, valKeteranganStatus, valAstatus, valAketerangan;
    private CircularProgressView spinner, spinnerAstatus;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AntaranAdapter antaranAdapter;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private RadioGroup radioGroup;
    private FloatingActionButton fab;
    private FrameLayout frameNoData;
    private Snackbar snackbar;
    private AlertDialog adus, adjs, adp, adk, adi;

    private int animationDuration, countData;

    AwesomeValidation awesomeValidation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(MY_LOG, "onCreateView");
        rootView = inflater.inflate(R.layout.antaran_tab_layout, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        spinner = (CircularProgressView) rootView.findViewById(R.id.spinner);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        tvCountData = (TextView) rootView.findViewById(R.id.countData);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        frameNoData = (FrameLayout) rootView.findViewById(R.id.frameNoData);

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(MY_LOG, "onCreateOptionsMenu");
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchAkditem:
                Log.i(MY_LOG, "onOptionsItemSelected searchAkditem");
                searchAkditem(item);
                return true;
            case R.id.scanAkditem:
                Log.i(MY_LOG, "onOptionsItemSelected scanAkditem");
                startBarcodeScannerActvity();
                return true;
            case R.id.updateKolektif:
                Log.i(MY_LOG, "onOptionsItemSelected updateKolektif");
                startUpdateKolektifActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getIntentResult() {
        Log.i(MY_LOG, "getIntentResult");
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("resultScan")) {
            String resultScan = intent.getStringExtra("resultScan");
            if (resultScan.equals("null")) {
                Toast.makeText(getContext(), "No Barcode Tidak Ditemukan Pada Delivery Order", Toast.LENGTH_LONG).show();
            } else {
                alertDialogUpdateStatus(resultScan);
            }
        }
    }

    private void startBarcodeScannerActvity() {
        Intent intent = new Intent(getActivity(), BarcodeScannerActivity.class);
        intent.putExtra("anippos", anippos);
        startActivity(intent);
    }

    private void startUpdateKolektifActivity() {
        Intent intent = new Intent(getActivity(), UpdateKolektifActivity.class);
        intent.putExtra("anippos", anippos);
        startActivity(intent);
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

    private void getAllAdrantaran() {
        Log.i(MY_LOG, "getAllAdrantaran");
        String param1 = "?status=0";
        String param2 = "&anippos=" + anippos;
        String param3 = "&akditem=" + akditem;
        String params = param1 + param2 + param3;
        StringRequest stringRequest = new StringRequest(DBConfig.JSON_URL_ADRANTARAN + params,
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
                        String se = "0";
                        showSnackbar(STR_ERROR, se);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void showAdrantaran(String json) {
        Log.i(MY_LOG, "showAdrantaran");
        AntaranParseJSON pj = new AntaranParseJSON(json);
        pj.parseJSON();
        Log.i(MY_LOG, "showAdrantaran parseJSON");
        antaranAdapter = new AntaranAdapter(
                getActivity(), AntaranParseJSON.akditem, AntaranParseJSON.akdstatus, AntaranParseJSON.awklokal,
                AntaranParseJSON.adraAketerangan, AntaranParseJSON.adrsAketerangan, AntaranParseJSON.astatuskirim, AntaranParseJSON.ado);
        antaranAdapter.notifyDataSetChanged();
        if (antaranAdapter.getCount() == 0) {
            frameNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        } else {
            frameNoData.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            listView.setAdapter(antaranAdapter);
            countData = listView.getAdapter().getCount();
            tvCountData.setText("" + countData);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(MY_LOG, "onItemClick");
        String itemValue = String.valueOf(listView.getItemAtPosition(i));
        alertDialogUpdateStatus(itemValue);
    }

    private void alertDialogUpdateStatus(String resAkditem) {
        Log.i(MY_LOG, "alertDialogUpdateStatus");
        valAkditem = resAkditem;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_dialog_update_status, null);
        TextView tvValAkditem = (TextView) view.findViewById(R.id.tvValAkditem);
        tvValAkditem.setText(resAkditem);


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
                        addRadioButton(response);

                        spinnerAstatus.animate().alpha(0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                spinnerAstatus.setVisibility(View.GONE);
                            }
                        });
                        antaranAdapter.notifyDataSetChanged();
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

    private void addRadioButton(String json) {
        Log.i(MY_LOG, "addRadioButton");
        AdrstatusParseJSON apj = new AdrstatusParseJSON(json);
        apj.parseJSON();
        Log.i(MY_LOG, "addRadioButton parseJSON");

        for (int i = 0; i < AdrstatusParseJSON.jsonArrayLength; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(Integer.parseInt(AdrstatusParseJSON.astatus[i]));
            radioButton.setText(AdrstatusParseJSON.aketerangan[i]);
            radioButton.setTextSize(16);
            //radioButton.setTextColor(getResources().getColor(R.color.colorSecondaryText));
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

    private void showSnackbar(String s, String se) {
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        //View rootView = inflater.inflate(R.layout.activity_main, null);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout);

        if (se.equals("0")) {
            snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ulangi", new View.OnClickListener() {
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

    /*private void alertDialogInformasi(String s) {
        Log.i(MY_LOG, "alertDialogInformasi");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_dialog_informasi, null);
        TextView tvInformasi = (TextView) view.findViewById(R.id.tvInformasi);
        tvInformasi.setText(s);

        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
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
    }*/

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), "Long click", Toast.LENGTH_SHORT).show();
        return true;
    }
}
