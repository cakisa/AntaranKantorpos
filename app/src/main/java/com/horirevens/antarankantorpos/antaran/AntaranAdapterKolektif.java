package com.horirevens.antarankantorpos.antaran;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.libs.MyDate;

/**
 * Created by horirevens on 12/27/16.
 */
public class AntaranAdapterKolektif extends ArrayAdapter<String> {
    private String[] akditem;
    private String[] awktlokal;
    private String[] adrsAketerangan;
    private int sizeAdapter;
    private Activity context;

    boolean[] itemChecked;

    public AntaranAdapterKolektif(
            Activity context, String[] akditem, String[] awktlokal, String[] adrsAketerangan, int sizeAdapter
    ) {
        super(context, R.layout.listview_tab, akditem);
        this.context = context;
        this.akditem = akditem;
        this.awktlokal = awktlokal;
        this.adrsAketerangan = adrsAketerangan;
        this.sizeAdapter = sizeAdapter;
        itemChecked = new boolean[this.sizeAdapter];
    }

    public boolean getItemChecked(int position) {
        return itemChecked[position];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.listview_update_kolektif, null, true);

        TextView tvAkditem = (TextView) convertView.findViewById(R.id.akditem);
        TextView tvAwtlokal = (TextView) convertView.findViewById(R.id.awktlokal);
        TextView tvAketerangan = (TextView) convertView.findViewById(R.id.aketerangan);
        final CheckBox cbAkditem = (CheckBox) convertView.findViewById(R.id.cbAkditem);

        MyDate myDate = new MyDate();
        tvAkditem.setText(akditem[position]);
        tvAwtlokal.setText(myDate.datetimeIndo(awktlokal[position]));
        tvAketerangan.setText(adrsAketerangan[position]);
        cbAkditem.setTag(akditem[position]);
        //cbAkditem.setChecked(false);

        if (itemChecked[position]) {
            cbAkditem.setChecked(true);
        } else {
            cbAkditem.setChecked(false);
        }

        cbAkditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAkditem.isChecked()) {
                    itemChecked[position] = true;
                    //Toast.makeText(getContext(), "Checked " + itemChecked[position], Toast.LENGTH_SHORT).show();
                } else {
                    itemChecked[position] = false;
                    //Toast.makeText(getContext(), "UnChecked " + itemChecked[position], Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
