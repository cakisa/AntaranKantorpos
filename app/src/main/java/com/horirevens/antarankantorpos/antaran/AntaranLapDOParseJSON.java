package com.horirevens.antarankantorpos.antaran;

import android.util.Log;

import com.horirevens.antarankantorpos.DBConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by horirevens on 1/16/17.
 */
public class AntaranLapDOParseJSON {
    public static String[] ado;
    public static String[] proses;
    public static String[] berhasil;
    public static String[] gagal;
    public static String[] jml_item;

    public static final String MY_LOG = "log_message";


    private JSONArray jsonArray = null;
    private JSONObject jsonObject = null;
    private String json;

    public AntaranLapDOParseJSON(String json){
        this.json = json;
    }

    public void parseJSON(){
        Log.i(MY_LOG, "parseJSON");
        try {
            Log.i(MY_LOG, "parseJSON try");
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray(DBConfig.TAG_JSON_ARRAY);

            ado = new String[jsonArray.length()];
            proses = new String[jsonArray.length()];
            berhasil = new String[jsonArray.length()];
            gagal = new String[jsonArray.length()];
            jml_item = new String[jsonArray.length()];

            for(int i=0;i<jsonArray.length();i++){
                Log.i(MY_LOG, "parseJSON looping");
                JSONObject jo = jsonArray.getJSONObject(i);
                Log.i(MY_LOG, "parseJSON JSONObject");
                ado[i] = jo.getString(DBConfig.TAG_ADO);
                proses[i] = jo.getString(DBConfig.TAG_PROSES);
                berhasil[i] = jo.getString(DBConfig.TAG_BERHASIL);
                gagal[i] = jo.getString(DBConfig.TAG_GAGAL);
                jml_item[i] = jo.getString(DBConfig.TAG_JML_ITEM);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
