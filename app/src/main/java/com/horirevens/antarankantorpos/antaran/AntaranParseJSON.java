package com.horirevens.antarankantorpos.antaran;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by horirevens on 12/16/16.
 */
public class AntaranParseJSON {
    public static String[] akditem;
    public static String[] awklokal;
    public static String[] akdstatus;
    public static String[] adraAketerangan;
    public static String[] adrsAketerangan;
    public static String[] astatuskirim;

    public static final String MY_LOG = "log_message";
    public static final String TAG_JSON_ARRAY="array";
    public static final String TAG_AKDITEM = "akditem";
    public static final String TAG_AKDSTATUS = "akdstatus";
    public static final String TAG_AWKTLOKAL = "awktlokal";
    public static final String TAG_ADRA_AKETERANGAN = "ada_aketerangan";
    public static final String TAG_ADRS_AKETERANGAN = "ads_aketerangan";
    public static final String TAG_ASTATUSKIRIM = "astatuskirim";

    private JSONArray jsonArray = null;
    private JSONObject jsonObject = null;
    private String json;

    public AntaranParseJSON(String json){
        this.json = json;
    }

    public void parseJSON(){
        Log.i(MY_LOG, "parseJSON");
        try {
            Log.i(MY_LOG, "parseJSON try");
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray(TAG_JSON_ARRAY);

            akditem = new String[jsonArray.length()];
            akdstatus = new String[jsonArray.length()];
            awklokal = new String[jsonArray.length()];
            adraAketerangan = new String[jsonArray.length()];
            adrsAketerangan = new String[jsonArray.length()];
            astatuskirim = new String[jsonArray.length()];


            for(int i=0;i<jsonArray.length();i++){
                Log.i(MY_LOG, "parseJSON looping");
                JSONObject jo = jsonArray.getJSONObject(i);
                Log.i(MY_LOG, "parseJSON JSONObject");
                akditem[i] = jo.getString(TAG_AKDITEM);
                akdstatus[i] = jo.getString(TAG_AKDSTATUS);
                awklokal[i] = jo.getString(TAG_AWKTLOKAL);
                adraAketerangan[i] = jo.getString(TAG_ADRA_AKETERANGAN);
                adrsAketerangan[i] = jo.getString(TAG_ADRS_AKETERANGAN);
                astatuskirim[i] = jo.getString(TAG_ASTATUSKIRIM);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
