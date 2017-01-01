package com.horirevens.antarankantorpos.antaran;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.horirevens.antarankantorpos.R;

/**
 * Created by horirevens on 12/19/16.
 */
public class AdrstatusAdapter extends ArrayAdapter<String> {
    private String[] astatus;
    private String[] aketerangan;
    private Activity context;

    public AdrstatusAdapter(Activity context, String[] astatus, String[] aketerangan) {
        super(context, R.layout.alert_dialog_jenis_status, astatus);
        this.context = context;
        this.astatus = astatus;
        this.aketerangan = aketerangan;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.alert_dialog_jenis_status, null, true);

        /*RadioButton radioButton = new RadioButton(getContext());
        radioButton.setId(Integer.parseInt(astatus[position]));
        radioButton.setText(aketerangan[position]);
        radioButton.setTextSize(18);
        radioGroup.addView(radioButton);*/

        return convertView;
    }
}
