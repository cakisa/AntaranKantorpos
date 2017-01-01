package com.horirevens.antarankantorpos.antaran;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.horirevens.antarankantorpos.R;

/**
 * Created by horirevens on 12/27/16.
 */
public class AntaranAdapterCheckbox extends ArrayAdapter<String> {
    private String[] akditem;
    private Activity context;

    public AntaranAdapterCheckbox(Activity context, String[] akditem) {
        super(context, R.layout.listview_checklist, akditem);
        this.context = context;
        this.akditem = akditem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.listview_checklist, null, true);

        final CheckBox cbAkditem = (CheckBox) convertView.findViewById(R.id.cbAkditem);

        cbAkditem.setText(akditem[position]);
        cbAkditem.setTextSize(18);

        cbAkditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAkditem.isChecked()) {
                    Toast.makeText(getContext(), "Is Checked", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return convertView;
    }
}
