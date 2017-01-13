package com.horirevens.antarankantorpos.antaran;

import android.util.Log;

import com.horirevens.antarankantorpos.DBConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by horirevens on 12/19/16.
 */
public class AdrstatusParseJSON {
    public static String[] astatus;
    public static String[] aketerangan;
    public static int jsonArrayLength;

    public static final String MY_LOG = "log_AdrstatusParseJSON";

    private JSONArray jsonArray = null;
    private JSONObject jsonObject = null;
    private String json;

    public AdrstatusParseJSON(String json){
        this.json = json;
    }

    public void parseJSON(){
        Log.i(MY_LOG, "parseJSON");
        try {
            Log.i(MY_LOG, "parseJSON try");
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray(DBConfig.TAG_JSON_ARRAY);

            astatus = new String[jsonArray.length()];
            aketerangan = new String[jsonArray.length()];
            jsonArrayLength = jsonArray.length();

            for(int i=0;i<jsonArray.length();i++){
                Log.i(MY_LOG, "parseJSON looping");
                JSONObject jo = jsonArray.getJSONObject(i);
                Log.i(MY_LOG, "parseJSON JSONObject");
                astatus[i] = jo.getString(DBConfig.TAG_ASTATUS);
                aketerangan[i] = jo.getString(DBConfig.TAG_AKETERANGAN);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
