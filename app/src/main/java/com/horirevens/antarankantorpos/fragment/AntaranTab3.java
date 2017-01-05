package com.horirevens.antarankantorpos.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.antaran.AntaranAdapter;
import com.horirevens.antarankantorpos.antaran.AntaranParseJSON;

/**
 * Created by horirevens on 11/25/16.
 */
public class AntaranTab3 extends Fragment {
    public static final String JSON_URL_ADRANTARAN = "http://mob.agenposedo.com/adrantaran.php";
    public static final String MY_LOG = "log_AntaranTab3";

    private ListView listView;
    private View rootView;
    private TextView tvCountData;
    private String anippos, akditem;
    private CircularProgressView spinner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AntaranAdapter antaranAdapter;
    private SearchView searchView;
    private FloatingActionButton fab;
    private FrameLayout frameNoData;

    private int animationDuration, countData;

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
            listView.setVisibility(View.GONE);
            frameNoData.setVisibility(View.GONE);
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
        menu.findItem(R.id.scanAkditem).setVisible(false);
        menu.findItem(R.id.updateKolektif).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchAkditem:
                Log.i(MY_LOG, "onOptionsItemSelected searchAkditem");
                searchAkditem(item);
                return true;
            /*case R.id.scanAkditem:
                Log.i(MY_LOG, "onOptionsItemSelected scanAkditem");
                IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
                scanIntegrator.initiateScan();
                return  true;*/
            default:
                return super.onOptionsItemSelected(item);
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
        String param1 = "?status=2";
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
                        antaranAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MY_LOG, "onErrorResponse");
                        Toast.makeText(getContext(), "Gangguan Koneksi. Keluar dan Jalankan Kembali", Toast.LENGTH_LONG).show();
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
                AntaranParseJSON.adraAketerangan, AntaranParseJSON.adrsAketerangan, AntaranParseJSON.astatuskirim);
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
}
