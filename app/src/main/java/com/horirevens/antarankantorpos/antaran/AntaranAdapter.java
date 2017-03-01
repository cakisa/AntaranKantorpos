package com.horirevens.antarankantorpos.antaran;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.libs.MyDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horirevens on 2/11/17.
 */

public class AntaranAdapter extends RecyclerView.Adapter<AntaranAdapter.MyViewHolder> {
    private List<Antaran> antaranList;
    private ArrayList<Antaran> arrayList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Antaran antaran);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView akditem, awktlokal, aketerangan;
        public ImageView akdstatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            akditem = (TextView) itemView.findViewById(R.id.akditem);
            awktlokal = (TextView) itemView.findViewById(R.id.awktlokal);
            aketerangan = (TextView) itemView.findViewById(R.id.aketerangan);
            akdstatus = (ImageView) itemView.findViewById(R.id.akdstatus);
        }

        public void bind (final Antaran antaran, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(antaran);
                }
            });
        }
    }

    public AntaranAdapter(List<Antaran> antaranList, Context context, OnItemClickListener listener) {
        this.antaranList = antaranList;
        this.context = context;
        this.listener = listener;
        arrayList = new ArrayList<>();
        arrayList.addAll(antaranList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_antaran, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Antaran antaran = antaranList.get(position);
        MyDate myDate = new MyDate();

        holder.bind(antaranList.get(position), listener);
        holder.akditem.setText(antaran.getAkditem());
        if (antaran.getAkdstatus().equals("6207") || antaran.getAkdstatus().equals("6208") ||
                antaran.getAkdstatus().equals("6209") || antaran.getAkdstatus().equals("6210") ||
                antaran.getAkdstatus().equals("6211") || antaran.getAkdstatus().equals("6212") ||
                antaran.getAkdstatus().equals("6213") || antaran.getAkdstatus().equals("6214") ||
                antaran.getAkdstatus().equals("6215") || antaran.getAkdstatus().equals("6216") ||
                antaran.getAkdstatus().equals("6217") || antaran.getAkdstatus().equals("6218")) {
            holder.awktlokal.setText(myDate.datetimeIndo(antaran.getAwktlokal()) + "\nNo. DO " + antaran.getAdo());
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
            holder.awktlokal.setText(myDate.datetimeIndo(antaran.getAwktlokal()) + "\nNo. DO " + antaran.getAdo());
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
    }

    @Override
    public int getItemCount() {
        return antaranList.size();
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
