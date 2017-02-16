package com.horirevens.antarankantorpos.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.horirevens.antarankantorpos.DBConfig;
import com.horirevens.antarankantorpos.KolektifActivity;
import com.horirevens.antarankantorpos.LapDOActivity;
import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.about.AboutActivity;
import com.horirevens.antarankantorpos.antaran.Antaran;
import com.horirevens.antarankantorpos.antaran.AntaranAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by horirevens on 1/18/17.
 */
public class AntaranTab2 extends Fragment {
    public static final String STR_ERROR = "Gagal memuat data";
    public static final String MY_LOG = "log_AntaranTab2";

    private View rootView;
    private RecyclerView recyclerView;
    private String anippos;
    private TextView tvCountData;
    private CircularProgressView spinner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private FrameLayout frameNoData;
    private Snackbar snackbar;
    private SearchView searchView;

    private int animationDuration, countData;

    private AntaranAdapter antaranAdapter;
    private ArrayList<Antaran> antaranList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(MY_LOG, "onCreateView");
        rootView = inflater.inflate(R.layout.tab_layout_antaran, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        spinner = (CircularProgressView) rootView.findViewById(R.id.spinner);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        tvCountData = (TextView) rootView.findViewById(R.id.countData);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        frameNoData = (FrameLayout) rootView.findViewById(R.id.frameNoData);

        anippos = getArguments().getString("anippos");
        frameNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        getAllAdrantaran();
        swipeRefresh();

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.i(MY_LOG, "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            Log.i(MY_LOG, "setUserVisibleHint isVisible");
            recyclerView.setVisibility(View.GONE);
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
        String param1 = "?status=1";
        String param2 = "&anippos=" + anippos;
        String params = param1 + param2;
        StringRequest stringRequest = new StringRequest(DBConfig.JSON_URL_ADRANTARAN + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MY_LOG, "onResponse");
                        recyclerView.setAlpha(0f);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.animate().alpha(1f).setDuration(animationDuration).setListener(null);
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
        antaranAdapter = new AntaranAdapter(antaranList, getContext(), new AntaranAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Antaran antaran) {

            }
        });

        if (antaranAdapter.getItemCount() == 0) {
            frameNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        } else {
            frameNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mlayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(antaranAdapter);

            countData = recyclerView.getAdapter().getItemCount();
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
        menu.findItem(R.id.scanAkditem).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchAkditem:
                Log.i(MY_LOG, "onOptionsItemSelected searchAkditem");
                searchAkditem(item);
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
                                antaranAdapter.filter(s);
                                recyclerView.invalidate();
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
                        recyclerView.setVisibility(View.GONE);
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
}
