package com.horirevens.antarankantorpos.antaran;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.horirevens.antarankantorpos.R;

/**
 * Created by horirevens on 1/15/17.
 */
public class AntaranAdapterLaporanDO extends ArrayAdapter<String> {
    private String[] ado;
    private String[] proses;
    private String[] berhasil;
    private String[] gagal;
    private String[] jml_item;
    private Activity context;

    public AntaranAdapterLaporanDO(
            Activity context, String[] ado, String[] proses, String[] berhasil,
            String[] gagal, String[] jml_item
    ) {
        super(context, R.layout.listview_tab, ado);
        this.context = context;
        this.ado = ado;
        this.proses = proses;
        this.berhasil = berhasil;
        this.gagal = gagal;
        this.jml_item = jml_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.listview_laporan_do, null, true);

        TextView tvAdo = (TextView) convertView.findViewById(R.id.ado);
        TextView tvAdoKeterangan = (TextView) convertView.findViewById(R.id.adoKeterangan);
        ImageView ivAdoStatus = (ImageView) convertView.findViewById(R.id.adoStatus);

        tvAdo.setText(ado[position]);
        tvAdoKeterangan.setText(
                "Sisa: " + proses[position] + " | " +
                "Berhasil: " + berhasil[position] + " | " +
                "Gagal: " + gagal[position] + " | " +
                "Jml Item: " + jml_item[position]
        );
        if(proses[position].equals("0")) {
            ivAdoStatus.setImageResource(R.drawable.ba);
        } else {
            ivAdoStatus.setImageResource(R.drawable.ga);
        }

        return convertView;
    }
}
