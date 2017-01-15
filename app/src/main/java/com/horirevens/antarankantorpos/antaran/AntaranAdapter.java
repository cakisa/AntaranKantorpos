package com.horirevens.antarankantorpos.antaran;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.libs.MyDate;

/**
 * Created by horirevens on 12/16/16.
 */
public class AntaranAdapter extends ArrayAdapter<String> {
    private String[] akditem;
    private String[] akdstatus;
    private String[] awktlokal;
    private String[] adraAketerangan;
    private String[] adrsAketerangan;
    private String[] astatuskirim;
    private String[] ado;
    private Activity context;

    public AntaranAdapter(
            Activity context, String[] akditem, String[] akdstatus, String[] awktlokal,
            String[] adraAketerangan, String[] adrsAketerangan, String[] astatuskirim, String[] ado
    ) {
        super(context, R.layout.listview_tab, akditem);
        this.context = context;
        this.akditem = akditem;
        this.awktlokal = awktlokal;
        this.akdstatus = akdstatus;
        this.adraAketerangan = adraAketerangan;
        this.adrsAketerangan = adrsAketerangan;
        this.astatuskirim = astatuskirim;
        this.ado = ado;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.listview_tab, null, true);
        TextView tvAkditem = (TextView) convertView.findViewById(R.id.akditem);
        TextView tvAwtlokal = (TextView) convertView.findViewById(R.id.awktlokal);
        TextView tvAketerangan = (TextView) convertView.findViewById(R.id.aketerangan);
        ImageView tvAkdstatus = (ImageView) convertView.findViewById(R.id.akdstatus);

        MyDate myDate = new MyDate();
        tvAkditem.setText(akditem[position]);
        tvAwtlokal.setText(myDate.datetimeIndo(awktlokal[position]) + " | No. DO " + ado[position]);
        if (akdstatus[position].equals("6207") || akdstatus[position].equals("6208") ||
                akdstatus[position].equals("6209") || akdstatus[position].equals("6210") ||
                akdstatus[position].equals("6211") || akdstatus[position].equals("6212") ||
                akdstatus[position].equals("6213") || akdstatus[position].equals("6214") ||
                akdstatus[position].equals("6215") || akdstatus[position].equals("6216") ||
                akdstatus[position].equals("6217") || akdstatus[position].equals("6218")) {
            if (astatuskirim[position].equals("2")) {
                tvAkdstatus.setImageResource(R.drawable.ba_android);
                tvAketerangan.setText(adrsAketerangan[position] + " (" + adraAketerangan[position] + ")");
            } else if (astatuskirim[position].equals("3")) {
                tvAkdstatus.setImageResource(R.drawable.ba_desktop);
                tvAketerangan.setText(adrsAketerangan[position] + " (" + adraAketerangan[position] + ")");
            } else {
                tvAkdstatus.setImageResource(R.drawable.ba);
                tvAketerangan.setText(adrsAketerangan[position] + " (" + adraAketerangan[position] + ")");
            }
        } else if (akdstatus[position].equals("6221") || akdstatus[position].equals("6220") ||
                akdstatus[position].equals("6238")) {
            if (astatuskirim[position].equals("2")) {
                tvAkdstatus.setImageResource(R.drawable.ga_android);
                tvAketerangan.setText(adrsAketerangan[position] + " (" + adraAketerangan[position] + ")");
            } else if (astatuskirim[position].equals("3")) {
                tvAkdstatus.setImageResource(R.drawable.ga_desktop);
                tvAketerangan.setText(adrsAketerangan[position] + " (" + adraAketerangan[position] + ")");
            } else {
                tvAkdstatus.setImageResource(R.drawable.ga);
                tvAketerangan.setText(adrsAketerangan[position] + " (" + adraAketerangan[position] + ")");
            }
        } else {
            tvAkdstatus.setImageResource(R.drawable.do_proses);
            tvAketerangan.setText(adrsAketerangan[position]);
        }

        return convertView;
    }
}
