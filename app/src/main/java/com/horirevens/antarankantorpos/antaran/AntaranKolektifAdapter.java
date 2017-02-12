package com.horirevens.antarankantorpos.antaran;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.libs.MyDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horirevens on 2/12/17.
 */

public class AntaranKolektifAdapter extends RecyclerView.Adapter<AntaranKolektifAdapter.MyViewHolder> {
    private List<AntaranKolektif> antaranList;
    private ArrayList<AntaranKolektif> arrayList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView awktlokal, aketerangan;
        public CheckBox cbAkditem;

        public MyViewHolder(View itemView) {
            super(itemView);
            awktlokal = (TextView) itemView.findViewById(R.id.awktlokal);
            aketerangan = (TextView) itemView.findViewById(R.id.aketerangan);
            cbAkditem = (CheckBox) itemView.findViewById(R.id.cbAkditem);

            cbAkditem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    AntaranKolektif antaran = (AntaranKolektif) cb.getTag();
                    antaran.setSelected(cb.isChecked());
                }
            });
        }
    }

    public AntaranKolektifAdapter(ArrayList<AntaranKolektif> antaranList, Context context) {
        this.antaranList = antaranList;
        this.context = context;
        arrayList = new ArrayList<>();
        arrayList.addAll(antaranList);
    }
    
    @Override
    public AntaranKolektifAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_kolektif, parent, false);
        return new AntaranKolektifAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AntaranKolektifAdapter.MyViewHolder holder, int position) {
        MyDate myDate = new MyDate();
        AntaranKolektif antaran = antaranList.get(position);
        holder.cbAkditem.setChecked(antaran.isSelected());
        holder.cbAkditem.setText(antaran.getAkditem());
        holder.cbAkditem.setTag(antaran);
        holder.cbAkditem.setTextSize(24);
        holder.awktlokal.setText(myDate.datetimeIndo(antaran.getAwktlokal()));
        holder.aketerangan.setText("No. DO " + antaran.getAdo());
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
            for (AntaranKolektif antaranDetail : arrayList) {
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
