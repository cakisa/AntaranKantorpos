package com.horirevens.antarankantorpos.antaran;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.libs.MyDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horirevens on 1/30/17.
 */

public class AntaranAdapterDO extends ArrayAdapter<Antaran> {

    private List<Antaran> antaranList;
    private ArrayList<Antaran> arrayList;
    private Context context;

    public AntaranAdapterDO(List<Antaran> antaranList, Context context) {
        super(context, R.layout.listview_antaran_do, antaranList);
        this.antaranList = antaranList;
        this.context = context;
        arrayList = new ArrayList<>();
        arrayList.addAll(antaranList);
    }

    public int getCount() {
        return antaranList.size();
    }

    public Antaran getItem(int position) {
        return antaranList.get(position);
    }

    public long getItemId(int position) {
        return antaranList.get(position).hashCode();
    }

    private static class ViewHolder {
        public TextView akditem, awktlokal, aketerangan;
        public ImageView akdstatus;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AntaranAdapterDO.ViewHolder holder = new AntaranAdapterDO.ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_antaran_do, parent, false);
            holder.akditem = (TextView) convertView.findViewById(R.id.akditem);
            holder.awktlokal = (TextView) convertView.findViewById(R.id.awktlokal);
            holder.aketerangan = (TextView) convertView.findViewById(R.id.aketerangan);
            holder.akdstatus = (ImageView) convertView.findViewById(R.id.akdstatus);

            convertView.setTag(holder);
        } else {
            holder = (AntaranAdapterDO.ViewHolder) convertView.getTag();
        }

        MyDate myDate = new MyDate();
        Antaran antaran = antaranList.get(position);
        holder.akditem.setText(antaran.getAkditem());

        if (antaran.getAkdstatus().equals("6207") || antaran.getAkdstatus().equals("6208") ||
                antaran.getAkdstatus().equals("6209") || antaran.getAkdstatus().equals("6210") ||
                antaran.getAkdstatus().equals("6211") || antaran.getAkdstatus().equals("6212") ||
                antaran.getAkdstatus().equals("6213") || antaran.getAkdstatus().equals("6214") ||
                antaran.getAkdstatus().equals("6215") || antaran.getAkdstatus().equals("6216") ||
                antaran.getAkdstatus().equals("6217") || antaran.getAkdstatus().equals("6218")) {
            holder.awktlokal.setText(myDate.datetimeIndo(antaran.getAwktlokal()) + " | No. DO " + antaran.getAdo());
            //holder.aketerangan.setText(antaran.getAds_aketerangan() + " (" + antaran.getAda_aketerangan() + ")");
            if (antaran.getAstatuskirim().equals("2")) {
                holder.akdstatus.setImageResource(R.drawable.ba_android);
                holder.aketerangan.setText(antaran.getAds_aketerangan() + " (" + antaran.getAda_aketerangan() + ")");
            } else if (antaran.getAstatuskirim().equals("3")) {
                holder.akdstatus.setImageResource(R.drawable.ba_desktop);
                holder.aketerangan.setText(antaran.getAds_aketerangan() + " (" + antaran.getAda_aketerangan() + ")");
            } else {
                holder.akdstatus.setImageResource(R.drawable.ba);
                holder.aketerangan.setText(antaran.getAds_aketerangan() + " (" + antaran.getAda_aketerangan() + ")");
            }
        } else if (antaran.getAkdstatus().equals("6220") || antaran.getAkdstatus().equals("6221") ||
                antaran.getAkdstatus().equals("6238")) {
            holder.awktlokal.setText(myDate.datetimeIndo(antaran.getAwktlokal()) + " | No. DO " + antaran.getAdo());
            //holder.aketerangan.setText(antaran.getAds_aketerangan() + " (" + antaran.getAda_aketerangan() + ")");
            if (antaran.getAstatuskirim().equals("2")) {
                holder.akdstatus.setImageResource(R.drawable.ga_android);
                holder.aketerangan.setText(antaran.getAds_aketerangan() + " (" + antaran.getAda_aketerangan() + ")");
            } else if (antaran.getAstatuskirim().equals("3")) {
                holder.akdstatus.setImageResource(R.drawable.ga_desktop);
                holder.aketerangan.setText(antaran.getAds_aketerangan() + " (" + antaran.getAda_aketerangan() + ")");
            } else {
                holder.akdstatus.setImageResource(R.drawable.ga);
                holder.aketerangan.setText(antaran.getAds_aketerangan() + " (" + antaran.getAda_aketerangan() + ")");
            }
        } else {
            holder.akdstatus.setImageResource(R.drawable.do_proses);
            holder.awktlokal.setText(myDate.datetimeIndo(antaran.getAwktlokal()));
            holder.aketerangan.setText("No. DO " + antaran.getAdo());
        }

        return convertView;
    }

    public void filter(String akditem) {
        antaranList.clear();
        if (akditem.length() == 0) {
            antaranList.addAll(arrayList);
        } else {
            for (Antaran antaranDetail : arrayList) {
                if (akditem.length() != 0 && antaranDetail.getAkditem().contains(akditem)) {
                    antaranList.add(antaranDetail);
                } else if (akditem.length() != 0 && antaranDetail.getAkditem().contains(akditem)) {
                    antaranList.add(antaranDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}
